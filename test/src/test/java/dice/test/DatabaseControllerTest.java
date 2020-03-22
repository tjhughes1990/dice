package dice.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import dice.common.types.DiceRollCollection;
import dice.common.types.DiceRollType;
import dice.common.types.IdName;

/**
 * Test the DatabaseController.
 */
public class DatabaseControllerTest extends AbstractSystemTest {

    private static final long UNKNOWN_ID = -1L;
    private static final String INVALID_ID = "invalid";

    private static AtomicLong TEST_COUNTER = new AtomicLong(0L);

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
        assertTrue(idNames.size() >= 1);
        final Set<Long> idSet = idNames.parallelStream().map(i -> i.getId()).collect(Collectors.toSet());
        assertTrue(idSet.containsAll(Set.of(1L, 2L)) && !idSet.contains(0L));
    }

    private Set<IdName> getIdNames() throws Exception {
        final URI uri = URI.create(ROOT_URL + "getCollections");
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        final byte[] bytes = httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS).body();

        return new HashSet<>(Arrays.asList(MAPPER.readValue(bytes, IdName[].class)));
    }

    /**
     * Tests adding a new dice collection to the database.
     *
     * @throws Exception
     *             if the dice collection was not added successfully.
     *
     */
    @Test
    public void saveTest() throws Exception {
        final int originalEntries = getIdNames().size();

        final int statusCode = saveDiceRequest(
                new DiceRollCollection(null, generateCollectionName(), createDiceList()));

        assertEquals(HttpStatus.SC_OK, statusCode);
        assertEquals(originalEntries + 1, getIdNames().size());
    }

    /**
     * @return a unique, generated collection name to use for the test.
     */
    private static synchronized String generateCollectionName() {
        return "Test collection " + TEST_COUNTER.incrementAndGet();
    }

    /**
     * Send a save request to the database.
     *
     * @param collection
     *            the dice collection to be saved.
     *
     * @return the HTTP status code.
     *
     * @throws Exception
     *             if the request failed.
     */
    private int saveDiceRequest(final DiceRollCollection collection) throws Exception {
        final URI uri = URI.create(ROOT_URL + "save");
        final String jsonCollection = MAPPER.writeValueAsString(collection);
        final HttpRequest request = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString(jsonCollection))
                .setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON).build();
        return httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS).statusCode();
    }

    /**
     * Tests saving a collection with no dice.
     *
     * @throws Exception
     *             if the SQL query failed.
     */
    @Test
    public void saveNoDiceTest() throws Exception {
        final int originalEntries = getIdNames().size();
        final int statusCode = saveDiceRequest(
                new DiceRollCollection(null, generateCollectionName(), Collections.emptyList()));

        assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode);
        assertEquals(originalEntries, getIdNames().size());
    }

    /**
     * Tests saving a collection with null dice.
     *
     * @throws Exception
     *             if the SQL query failed.
     */
    @Test
    public void saveNullDiceTest() throws Exception {
        final int originalEntries = getIdNames().size();
        final int statusCode = saveDiceRequest(new DiceRollCollection(null, generateCollectionName(), null));

        assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode);
        assertEquals(originalEntries, getIdNames().size());
    }

    /**
     * Tests saving a collection with an empty name.
     *
     * @throws Exception
     *             if the SQL query failed.
     */
    @Test
    public void saveEmptyNameTest() throws Exception {
        final int originalEntries = getIdNames().size();
        final int statusCode = saveDiceRequest(new DiceRollCollection(null, "", createDiceList()));

        assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode);
        assertEquals(originalEntries, getIdNames().size());
    }

    /**
     * Tests saving a collection with a null name.
     *
     * @throws Exception
     *             if the SQL query failed.
     */
    @Test
    public void saveNullNameTest() throws Exception {
        final int originalEntries = getIdNames().size();
        final int statusCode = saveDiceRequest(new DiceRollCollection(null, null, createDiceList()));

        assertEquals(HttpStatus.SC_BAD_REQUEST, statusCode);
        assertEquals(originalEntries, getIdNames().size());
    }

    /**
     * Tests saving a collection with a non-unique name.
     *
     * @throws Exception
     *             if the SQL query failed.
     */
    @Test
    public void saveNonUniqueNameTest() throws Exception {
        final int originalEntries = getIdNames().size();
        final String name = generateCollectionName();
        final int statusCode = saveDiceRequest(new DiceRollCollection(null, name, createDiceList()));

        assertEquals(HttpStatus.SC_OK, statusCode);
        assertEquals(originalEntries + 1, getIdNames().size());

        final int statusCode2 = saveDiceRequest(new DiceRollCollection(null, name, createDiceList()));

        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, statusCode2);
        assertEquals(originalEntries + 1, getIdNames().size());
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

        // Add a dice collection to the database, ready for deletion.
        final String toDeleteName = generateCollectionName();
        saveDiceRequest(new DiceRollCollection(null, toDeleteName, createDiceList()));

        assertEquals(initialCollectionsCount + 1, getIdNames().size());

        // Get the ID to delete from the DB.
        final long toDeleteId = getIdNames().stream().filter(d -> d.getName().equals(toDeleteName)).findAny().get()
                .getId();

        // Delete the collection by ID from the database.
        final URI uri = URI.create(ROOT_URL + "delete?id=" + toDeleteId);
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        final HttpResponse<byte[]> response = httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10,
                TimeUnit.SECONDS);

        assertEquals(HttpStatus.SC_OK, response.statusCode());
        assertEquals(initialCollectionsCount, getIdNames().size());
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

        final DiceRollType diceRoll = diceTypes.get(0);
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
