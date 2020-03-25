package dice.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;

import com.fasterxml.jackson.databind.ObjectMapper;

import dice.common.DiceException;
import dice.common.types.DiceRollType;

/**
 * Docker runner for static initialisation.
 */
public abstract class AbstractSystemTest {

    protected static final String ROOT_URL = "http://localhost:8080/";

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    protected static final String CONTENT_TYPE_JSON = "application/json";

    protected final HttpClient httpClient;

    /**
     * Constructor.
     *
     * @throws RuntimeException
     *             if the HTTP client could not be initialised.
     */
    public AbstractSystemTest() throws RuntimeException {
        try {
            httpClient = HttpClient.newBuilder().sslContext(SSLContext.getDefault()).build();
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check that the docker containers are initialised.
     *
     * @throws DiceException
     *             if a REST response was not received.
     */
    @BeforeAll
    public static void checkConnection() throws DiceException {
        final int responseCode;
        try {
            final HttpClient httpClient = HttpClient.newBuilder().sslContext(SSLContext.getDefault()).build();
            final HttpRequest request = HttpRequest.newBuilder(URI.create(ROOT_URL)).GET().build();
            responseCode = httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS)
                    .statusCode();
        } catch (final ExecutionException | InterruptedException | NoSuchAlgorithmException | TimeoutException e) {
            throw new DiceException("Response not received successfully", e);
        }

        assertEquals(HttpStatus.SC_OK, responseCode);
    }

    /**
     * Create and return a new list of dice.
     *
     * @return the new list of dice.
     *
     * @throws DiceException
     *             if the dice were not created successfully.
     */
    protected static List<DiceRollType> createDiceList() throws DiceException {
        final List<DiceRollType> diceRolls = new ArrayList<>();
        diceRolls.add(new DiceRollType(0L, "Dice 0", 1, 10, 1));
        diceRolls.add(new DiceRollType(1L, "Dice 1", 1, 12, 1));

        return diceRolls;
    }
}
