package dice.core.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import dice.core.types.Dice;
import dice.core.types.DiceCollection;

public class DiceCollectionBinarySerialiserTests {

    private static final String TEST_NAME = "Dice collection";
    private static final String TEST_FILE = "dice." + DiceCollectionBinarySerialiser.EXT;

    @TempDir
    private File tempDir;

    @BeforeEach
    public void setup() {
        final File file = tempDir.toPath().resolve(TEST_FILE).toFile();
        file.delete();
    }

    @Test
    public void serialiseTest() {
        final DiceCollection diceCollection = new DiceCollection(TEST_NAME, Dice.D10, 3);
        final DiceCollectionSerialiser serialiser = new DiceCollectionBinarySerialiser();

        final File file = tempDir.toPath().resolve(TEST_FILE).toFile();
        serialiser.serialise(file, List.of(diceCollection));

        assertTrue(file.exists());
    }

    @Test
    public void deserialiseTest() {
        final DiceCollection originalDiceCollection = new DiceCollection(TEST_NAME, Dice.D10, 3);
        final DiceCollectionSerialiser serialiser = new DiceCollectionBinarySerialiser();

        final File file = tempDir.toPath().resolve(TEST_FILE).toFile();
        serialiser.serialise(file, List.of(originalDiceCollection));

        final List<DiceCollection> diceCollectionList = serialiser.deserialise(file).get();
        assertEquals(1, diceCollectionList.size());
        final DiceCollection deserialisedDiceCollection = diceCollectionList.get(0);
        assertEquals(originalDiceCollection.getName(), deserialisedDiceCollection.getName());
        assertEquals(originalDiceCollection.getDice(), deserialisedDiceCollection.getDice());
        assertEquals(originalDiceCollection.getCount(), deserialisedDiceCollection.getCount());
    }
}
