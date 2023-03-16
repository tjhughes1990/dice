package dice.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import dice.core.types.Dice;
import dice.core.types.DiceCollection;
import dice.core.types.DiceType;

/**
 * Tests the {@link DiceRoller}.
 */
public class DiceRollerTests {

    private static final Dice DICE = new Dice(DiceType.D6);

    /**
     * Test the dice roller.
     */
    @Test
    public void diceRollTest() {
        final DiceCollection diceCollection = new DiceCollection(DICE, 2);

        final DiceRoller diceRoller = new DiceRoller(0L, List.of(diceCollection));

        final List<Integer> rolls = diceRoller.performRoll();
        assertEquals(2, rolls.size());
        assertEquals(1, rolls.get(0));
        assertEquals(5, rolls.get(1));
    }

    /**
     * Test the dice roller.
     */
    @Test
    public void seedTest() {
        final DiceCollection diceCollection = new DiceCollection(DICE, 1);

        final DiceRoller diceRoller0 = new DiceRoller(0L, List.of(diceCollection));
        new DiceRoller(1L, List.of(diceCollection));

        final List<Integer> rolls0 = diceRoller0.performRoll();
        final List<Integer> rolls1 = diceRoller0.performRoll();

        assertNotEquals(rolls0.get(0), rolls1.get(0));
    }
}
