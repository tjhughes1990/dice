package dice.service.application;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLContext;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dice.common.DiceException;
import dice.common.types.DiceRollCollection;
import dice.common.types.DiceRollType;
import dice.service.roll.DiceRoller;
import dice.service.roll.RollAggregator;

@RestController
public class DiceController {

    private static final String DB_URL_ROOT = "http://db:8081/";
    private static final String DB_LOAD_URL = DB_URL_ROOT + "loadDice?id=";

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
     * Save a list of dice to the database.
     *
     * @param diceRollCollection
     *            the collection of dice to save to the database.
     *
     * @return {@code true} iff the dice collection was successfully saved to the database.
     */
    @PostMapping(value = "/save",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean save(@RequestBody final DiceRollCollection diceRollCollection) {
        // TODO
        return false;
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
        sendRequest(request);

        // TODO
        return Collections.emptyList();
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
    private byte[] sendRequest(final HttpRequest request) throws DiceException {
        try {
            return httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS).body();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new DiceException("Response not received from REST request", e);
        }
    }
}
