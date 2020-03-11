package dice.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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

    private static final long UNKNOWN_ID = -1L;
    private static final String INVALID_ID = "invalid";

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

    /**
     * Tests that dice collection names can be retrieved from the database.
     *
     * @throws Exception
     *             if there was an error retrieving dice collections from the database.
     */
    @Test
    public void getCollectionsTest() throws Exception {
        final Set<IdName> idNames = getIdNames();

        assertNotNull(idNames);
        assertEquals(2, idNames.size());
        final Set<Long> idSet = idNames.parallelStream().map(i -> i.getId()).collect(Collectors.toSet());
        assertEquals(Set.of(1L, 2L), idSet);
    }

    private Set<IdName> getIdNames() throws Exception {
        final URI uri = URI.create(ROOT_URL + "getCollections");
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        final byte[] bytes = httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS).body();

        return new HashSet<>(Arrays.asList(OBJECT_MAPPER.readValue(bytes, IdName[].class)));
    }

    /**
     * Tests deletion from the database.
     *
     * @throws Exception
     *             if the REST calls failed.
     */
    @Test
    public void deleteTest() throws Exception {
        // Get initial collections count.
        final int initialCollectionsCount = getIdNames().size();

        final URI uri = URI.create(ROOT_URL + "delete?id=2");
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS).body();

        assertEquals(initialCollectionsCount - 1, getIdNames().size());
    }

    /**
     * Test deletion of a dice collection ID that is not present in the database.
     *
     * @throws Exception
     *             if the deletion call failed.
     */
    @Test
    public void deleteUnknownId() throws Exception {
        // Get initial collections count.
        final int initialCollectionsCount = getIdNames().size();

        final URI uri = URI.create(ROOT_URL + "delete?id=" + UNKNOWN_ID);
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS).body();

        assertEquals(initialCollectionsCount, getIdNames().size());
    }

    /**
     * Test deletion with an invalid ID.
     *
     * @throws Exception
     *             if the REST call failed.
     */
    @Test
    public void deleteInvalidIdTest() throws Exception {
        final URI uri = URI.create(ROOT_URL + "delete?id=" + INVALID_ID);
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        final int statusCode = httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS)
                .statusCode();

        assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode);
    }

    /**
     * Test loading a dice collection from the database.
     *
     * @throws Exception
     *             if the dice collection could not be loaded from the database.
     */
    @Test
    public void loadTest() throws Exception {
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

    /**
     * Tests loading a dice collection using an ID that is not in the database.
     *
     * @throws Exception
     *             if an error occurred during the request.
     */
    @Test
    public void loadUnknownIdTest() throws Exception {
        final URI uri = URI.create(ROOT_URL + "load?id=" + UNKNOWN_ID);
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        final int responseCode = httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS)
                .statusCode();

        assertEquals(HttpStatus.SC_NO_CONTENT, responseCode);
    }

    /**
     * Tests loading a dice collection from the database with an invalid ID.
     *
     * @throws Exception
     *             if an unexpected exception occurred.
     */
    @Test
    public void loadInvalidIdTest() throws Exception {
        final URI uri = URI.create(ROOT_URL + "load?id=" + INVALID_ID);
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        final int responseCode = httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS)
                .statusCode();

        assertEquals(HttpStatus.SC_BAD_REQUEST, responseCode);
    }
}
