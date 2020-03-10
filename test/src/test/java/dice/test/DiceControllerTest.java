package dice.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import dice.common.types.DiceRollType;
import dice.common.types.IDiceRollType;
import dice.common.types.IdName;

/**
 * Test the DiceController.
 */
public class DiceControllerTest extends AbstractSystemTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final HttpClient httpClient;

    /**
     * Constructor.
     *
     * @throws NoSuchAlgorithmException
     *             if the HTTP client failed to initialise.
     */
    public DiceControllerTest() throws NoSuchAlgorithmException {
        httpClient = HttpClient.newBuilder().sslContext(SSLContext.getDefault()).build();
    }

    @Test
    public void getCollectionsTest() throws InterruptedException, ExecutionException, TimeoutException, IOException {

        final URI uri = URI.create(ROOT_URL + "getCollections");
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        final byte[] bytes = httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS).body();

        final Set<IdName> idNames = new HashSet<>(Arrays.asList(OBJECT_MAPPER.readValue(bytes, IdName[].class)));

        assertNotNull(idNames);
        assertEquals(2, idNames.size());
        final Set<Long> idSet = idNames.parallelStream().map(i -> i.getId()).collect(Collectors.toSet());
        assertEquals(Set.of(1L, 2L), idSet);
    }

    @Test
    public void loadTest() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        final URI uri = URI.create(ROOT_URL + "load?id=1");
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        final byte[] bytes = httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS).body();

        final ObjectMapper objectMapper = new ObjectMapper();

        final List<DiceRollType> diceTypes = Arrays.asList(objectMapper.readValue(bytes, DiceRollType[].class));
        assertNotNull(diceTypes);
        assertEquals(1, diceTypes.size());

        final IDiceRollType diceRoll = diceTypes.get(0);
        assertEquals(1, diceRoll.getMinResult());
        assertEquals(6, diceRoll.getMaxResult());
        assertEquals(3, diceRoll.getRollNumber());
        assertNull(diceRoll.getSumResult());
    }

    @Test
    public void loadUnknownIdTest() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        final URI uri = URI.create(ROOT_URL + "load?id=0");
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        final int responseCode = httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS)
                .statusCode();

        assertEquals(HttpStatus.SC_NO_CONTENT, responseCode);
    }

    @Test
    public void loadInvalidIdTest() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        final URI uri = URI.create(ROOT_URL + "load?id=invalid");
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        final int responseCode = httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS)
                .statusCode();

        assertEquals(HttpStatus.SC_BAD_REQUEST, responseCode);
    }
}
