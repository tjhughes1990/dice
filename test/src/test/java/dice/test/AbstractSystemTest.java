package dice.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;

import dice.common.DiceException;

/**
 * Docker runner for static initialisation.
 */
public abstract class AbstractSystemTest {

    protected static final String ROOT_URL = "http://localhost:8080/";

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
}
