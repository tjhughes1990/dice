package dice.service.application;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLContext;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import dice.common.DiceException;
import dice.common.types.DiceRollCollection;
import dice.common.types.DiceRollType;
import dice.common.types.IdName;
import dice.service.roll.DiceRoller;
import dice.service.roll.RollAggregator;

@RestController
public class DiceController {

    private static final String DB_URL_ROOT = "http://db:8081/";
    private static final String DB_GET_COLLECTIONS_URL = DB_URL_ROOT + "getCollections";
    private static final String DB_SAVE_URL = DB_URL_ROOT + "saveDice";
    private static final String DB_DELETE_URL = DB_URL_ROOT + "deleteDice?id=";
    private static final String DB_LOAD_URL = DB_URL_ROOT + "loadDice?id=";

    private static final String DESERIALISE_ERR_MSG = "Failed to deserialise response";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final HttpClient httpClient;
    private final DiceRoller diceRoller;

    /**
     * Constructor.
     *
     * @throws DiceException
     *             if the {@link DiceRoller} could not be created.
     */
    public DiceController() throws DiceException {
        try {
            httpClient = HttpClient.newBuilder().sslContext(SSLContext.getDefault()).build();
        } catch (final NoSuchAlgorithmException e) {
            throw new DiceException("Failed to create HTTP client", e);
        }
        diceRoller = new DiceRoller();
    }

    /**
     * Perform dice rolls.
     *
     * @param diceRolls
     *            the dice rolls to be performed.
     *
     * @return the dice rolls, with rolls performed.
     *
     * @throws DiceException
     *             if the dice definitions were invalid.
     */
    @PostMapping(value = "/roll",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<DiceRollType> roll(@RequestBody final List<DiceRollType> diceRolls) throws DiceException {
        final RollAggregator aggregator = new RollAggregator();
        diceRolls.stream().forEach(d -> aggregator.addDiceRoll(d));
        diceRoller.performRolls(aggregator);

        return diceRolls;
    }

    /**
     * Get a list of dice collections available in the database.
     *
     * @return a set of {@link IdName} objects corresponding to collections in the database.
     *
     * @throws DiceException
     *             if the response could not be deserialised.
     */
    @GetMapping(value = "/getCollections", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Set<IdName> getCollections() throws DiceException {
        final URI uri = URI.create(DB_GET_COLLECTIONS_URL);
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        final byte[] bytes = sendRequest(request);

        try {
            return new HashSet<>(Arrays.asList(OBJECT_MAPPER.readValue(bytes, IdName[].class)));
        } catch (final IOException e) {
            throw new DiceException(DESERIALISE_ERR_MSG, e);
        }
    }

    /**
     * Save a list of dice to the database.
     *
     * @param diceRollCollection
     *            the collection of dice to save to the database.
     *
     * @throws DiceException
     *             if the collection could not be saved to the database.
     */
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void save(@RequestBody final DiceRollCollection diceRollCollection) throws DiceException {
        final URI uri = URI.create(DB_SAVE_URL);

        // Serialise dice collection.
        final String body;
        try {
            body = OBJECT_MAPPER.writeValueAsString(diceRollCollection);
        } catch (final IOException e) {
            throw new DiceException("Failed to serialise dice collection");
        }

        final HttpRequest request = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString(body))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        sendRequest(request);
    }

    /**
     * Remove a dice collection from the database.
     *
     * @param collectionId
     *            the ID of the collection to remove from the database.
     *
     * @throws DiceException
     *             if the collection could not be deleted.
     */
    @GetMapping(value = "/delete")
    public void delete(@RequestParam(value = "id", required = true) final long collectionId) throws DiceException {
        final URI uri = URI.create(DB_DELETE_URL + collectionId);
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        sendRequest(request);
    }

    /**
     * Load a collection of dice from the database.
     *
     * @param collectionId
     *            the ID of the collection to load.
     *
     * @return a list of dice in the collection.
     *
     * @throws DiceException
     *             if no response was received from the database.
     */
    @GetMapping(value = "load", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<DiceRollType> load(@RequestParam(value = "id", required = true) final long collectionId)
            throws DiceException {

        final URI uri = URI.create(DB_LOAD_URL + collectionId);
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        final byte[] bytes = sendRequest(request);

        try {
            final List<DiceRollType> diceRolls = Arrays.asList(OBJECT_MAPPER.readValue(bytes, DiceRollType[].class));
            if (diceRolls.isEmpty()) {
                // Return a "No content" status if there were no dice for a given ID. Either the
                // collection contains no dice, or the ID doesn't appear in the database.
                throw new ResponseStatusException(HttpStatus.NO_CONTENT);
            }

            return diceRolls;
        } catch (final IOException e) {
            throw new DiceException(DESERIALISE_ERR_MSG, e);
        }
    }

    /**
     * Send and await a REST request.
     *
     * @param request
     *            the request to send.
     *
     * @return the body of the response.
     *
     * @throws DiceException
     *             if the request timed out.
     */
    private byte[] sendRequest(final HttpRequest request) {
        try {
            final HttpResponse<byte[]> response = httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10,
                    TimeUnit.SECONDS);
            if (HttpStatus.OK.value() != response.statusCode()) {
                throw new ResponseStatusException(HttpStatus.valueOf(response.statusCode()),
                        "Database REST call failed");
            }

            return response.body();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Response not received from REST request", e);
        }
    }
}
