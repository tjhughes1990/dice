package dice.common.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dice.common.DiceException;

/**
 * Dice Collection deserialisation tests.
 */
public class DiceCollectionTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String DICE_LIST_JSON = "[{\"id\":0,\"name\":\"D10\",\"minResult\":1,\"maxResult\":10,\"rollNumber\":1},{\"id\":1,\"name\":\"D12\",\"minResult\":1,\"maxResult\":12,\"rollNumber\":1}]";

    private static final long TEST_ID = 1L;
    private static final String TEST_NAME = "Test name";
    private static final List<DiceRollType> TEST_ROLLS;
    static {
        try {
            TEST_ROLLS = List.of(new DiceRollType(1, 6, 1));
        } catch (final DiceException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deserialiseCollectionTest() throws IOException {
        final String json = "{\"id\":1,\"name\":\"Test name\",\"diceRolls\":" + DICE_LIST_JSON + "}";

        final DiceRollCollection diceCollection = MAPPER.readValue(json.getBytes(), DiceRollCollection.class);
        assertNotNull(diceCollection);

        final List<DiceRollType> diceRolls = diceCollection.getDiceRolls();
        assertNotNull(diceRolls);
        assertEquals(2, diceRolls.size());
        assertNull(diceRolls.get(0).getSumResult());
    }

    @Test
    public void deserialiseNullIdTest() throws IOException {
        final String json = "{\"id\":null,\"name\":\"Test collection\",\"diceRolls\":" + DICE_LIST_JSON + "}";

        MAPPER.readValue(json.getBytes(), DiceRollCollection.class);
    }

    @Test
    public void deserialiseMissingIdTest() throws IOException {
        final String json = "{\"name\":\"Test collection\",\"diceRolls\":" + DICE_LIST_JSON + "}";

        try {
            MAPPER.readValue(json.getBytes(), DiceRollCollection.class);
            fail();
        } catch (final IOException e) {
            // Pass.
        }
    }

    @Test
    public void serialiseTest() throws JsonProcessingException {
        final DiceRollCollection collection = new DiceRollCollection(TEST_ID, TEST_NAME, TEST_ROLLS);
        final String json = MAPPER.writeValueAsString(collection);

        assertJson(json);
    }

    private static void assertJson(final String json) {
        assertNotNull(json);
        assertTrue(!json.isEmpty());
    }

    @Test
    public void serialiseNullIdTest() throws JsonProcessingException {
        final DiceRollCollection collection = new DiceRollCollection(null, TEST_NAME, TEST_ROLLS);
        final String json = MAPPER.writeValueAsString(collection);

        assertJson(json);
    }

    @Test
    public void serialiseEmptyDiceTest() throws JsonProcessingException {
        final DiceRollCollection collection = new DiceRollCollection(TEST_ID, TEST_NAME, Collections.emptyList());
        final String json = MAPPER.writeValueAsString(collection);

        assertJson(json);
    }

    @Test
    public void serialiseNullDiceTest() throws JsonProcessingException {
        final DiceRollCollection collection = new DiceRollCollection(TEST_ID, TEST_NAME, null);
        final String json = MAPPER.writeValueAsString(collection);

        assertJson(json);
    }
}
