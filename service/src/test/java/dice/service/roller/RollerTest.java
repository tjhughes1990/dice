package dice.service.roller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;

import dice.service.common.DiceException;
import dice.service.roll.DiceRoller;
import dice.service.roll.RollAggregator;
import dice.service.types.DiceRollType;
import dice.service.types.IDiceRollType;

/**
 * Tests the roller.
 */
public class RollerTest {

    private static final String EXPECTED_EXCEPTION_NOT_THROWN = "Expected exception was not thrown";

    /**
     * Test that a sum is produced.
     *
     * @throws DiceException
     *             if new dice or roller couldn't be created.
     */
    @Test
    public void rollerTest() throws DiceException {
        final RollAggregator aggregator = new RollAggregator();
        aggregator.addDiceRoll(new DiceRollType(4, 5, 1));

        final DiceRoller roller = new DiceRoller();
        roller.performRolls(aggregator);

        final List<IDiceRollType> diceRolls = aggregator.getDiceRolls();
        assertEquals(1, diceRolls.size());
        final Integer sum = diceRolls.get(0).getSumResult();

        assertNotNull(sum);
        assertTrue(sum == 4 || sum == 5);
    }

    /**
     * Test that an exception is thrown when trying to create an invalid dice.
     */
    @Test
    public void invalidDiceTest() {
        final String expectedErrMsg = "Invalid dice configuration specified";
        try {
            new DiceRollType(-1, 2, 1);
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (final DiceException e) {
            assertEquals(expectedErrMsg, e.getMessage());
        }

        try {
            new DiceRollType(3, 2, 1);
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (final DiceException e) {
            assertEquals(expectedErrMsg, e.getMessage());
        }

        try {
            new DiceRollType(0, 2, 0);
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (final DiceException e) {
            assertEquals(expectedErrMsg, e.getMessage());
        }
    }
}
