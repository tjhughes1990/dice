package dice.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import dice.core.types.Dice;
import dice.core.types.DiceCollection;
import dice.core.types.DiceRolls;

/**
 * Tests the {@link DiceRoller}.
 */
public class DiceRollerTests {

    private static final String NAME = "TestDiceCollection";

    /**
     * Test the dice roller.
     */
    @Test
    public void diceRollTest() {
        final DiceCollection diceCollection = new DiceCollection(NAME, Dice.D6, 2);

        final DiceRoller diceRoller = new DiceRoller(0L, List.of(diceCollection));
        diceRoller.performRoll();

        final DiceRolls rolls = diceCollection.getRolls();
        final List<Integer> rollValues = rolls.values();
        assertEquals(2, rollValues.size());
        assertEquals(1, rollValues.get(0));
        assertEquals(5, rollValues.get(1));
        assertEquals(6, rolls.total());
    }

    /**
     * Test the dice roller.
     */
    @Test
    public void seedTest() {
        final DiceCollection diceCollection0 = new DiceCollection(NAME, Dice.D6, 1);
        final DiceRoller diceRoller0 = new DiceRoller(0L, List.of(diceCollection0));
        diceRoller0.performRoll();
        final DiceRolls rolls0 = diceCollection0.getRolls();

        final DiceCollection diceCollection1 = new DiceCollection(NAME, Dice.D6, 1);
        final DiceRoller diceRoller1 = new DiceRoller(1L, List.of(diceCollection1));
        diceRoller1.performRoll();
        final DiceRolls rolls1 = diceCollection1.getRolls();

        assertNotEquals(rolls0.values().get(0), rolls1.values().get(0));
    }

    @Test
    public void independenceTest() {
        final List<DiceCollection> diceCollectionList = List.of(new DiceCollection(NAME), new DiceCollection(NAME));
        final DiceRoller diceRoller = new DiceRoller(0L, diceCollectionList);
        diceRoller.performRoll();

        final int value0 = diceCollectionList.get(0).getRolls().values().get(0);
        final int value1 = diceCollectionList.get(1).getRolls().values().get(0);

        assertNotEquals(value0, value1);
    }
}
