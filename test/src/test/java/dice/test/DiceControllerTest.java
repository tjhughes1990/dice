package dice.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;

import dice.common.types.DiceRollType;

/**
 * Test the DiceController.
 */
public class DiceControllerTest extends AbstractSystemTest {

    /**
     * Test that a roll can be requested successfully.
     *
     * @throws Exception
     *             if an unexpected exception occurred.
     */
    @Test
    public void rollTest() throws Exception {
        final List<DiceRollType> requestDiceList = createDiceList();
        final TypeReference<List<DiceRollType>> typeRef = new TypeReference<>() {
        };
        final String json = MAPPER.writerFor(typeRef).writeValueAsString(requestDiceList);
        final HttpResponse<byte[]> response = sendRoll(json);

        final List<DiceRollType> responseDiceList = MAPPER.readerFor(typeRef).readValue(response.body());

        assertEquals(HttpStatus.SC_OK, response.statusCode());
        assertNotNull(responseDiceList);
        assertTrue(!responseDiceList.isEmpty());
        assertEquals(requestDiceList.size(), responseDiceList.size());

        final Integer sum = responseDiceList.get(0).getSumResult();
        assertNotNull(sum);
        assertTrue(sum >= 1);
    }

    /**
     * Make REST call to roll.
     *
     * @param body
     *            the body of the POST request.
     *
     * @return the HTTP status code.
     *
     * @throws Exception
     *             if there was an error processing the request.
     */
    private HttpResponse<byte[]> sendRoll(final String body) throws Exception {
        final URI uri = URI.create(ROOT_URL + "roll");
        final HttpRequest request = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString(body))
                .setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON).build();
        return httpClient.sendAsync(request, BodyHandlers.ofByteArray()).get(10, TimeUnit.SECONDS);
    }

    /**
     * Test behaviour when a the request JSON is invalid.
     *
     * @throws Exception
     *             if an unexpected exception occurred.
     */
    @Test
    public void rollInvalidRequestTest() throws Exception {
        final HttpResponse<byte[]> response = sendRoll("{\"invalidProp\": \"invalidValue\"}");

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode());
    }

    /**
     * Test behaviour when a roll is requested with a null body.
     *
     * @throws Exception
     *             if an unexpected exception occurred.
     */
    @Test
    public void rollNullDiceTest() throws Exception {
        final HttpResponse<byte[]> response = sendRoll(MAPPER.writeValueAsString(null));

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode());
    }

    /**
     * Test behaviour when a roll is requested with no dice.
     *
     * @throws Exception
     *             if an unexpected exception occurred.
     */
    @Test
    public void rollEmptyDiceTest() throws Exception {
        final String emptyJsonList = "[]";
        final HttpResponse<byte[]> response = sendRoll(emptyJsonList);

        assertEquals(HttpStatus.SC_OK, response.statusCode());
        assertArrayEquals(emptyJsonList.getBytes(), response.body());
    }

    /**
     * Test behaviour when a roll is requested with invalid dice.
     *
     * @throws Exception
     *             if an unexpected exception occurred.
     */
    @Test
    public void rollInvalidDiceTest() throws Exception {
        final String invalidDiceJson = "[{\"minResult\":2,\"maxResult\":1,\"rollNumber\":1}]";
        final HttpResponse<byte[]> response = sendRoll(invalidDiceJson);

        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.statusCode());
    }
}
