package dice.service.roller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dice.common.DiceException;
import dice.common.types.DiceRollType;
import dice.common.types.IDiceRollType;
import dice.service.roll.DiceRoller;
import dice.service.roll.RollAggregator;

/**
 * Tests the roller.
 */
public class RollerTest {

    private static final String EXPECTED_EXCEPTION_NOT_THROWN = "Expected exception was not thrown";

    private static final long TEST_ID = 0L;
    private static final String TEST_NAME = "Test";

    private static final int RETRY_ATTEMPTS = 3;
    private static final double ACCEPTABLE_COEFF_VAR = 0.05;
    private static final String NL = "\n";

    private static DiceRoller DICE_ROLLER;

    @BeforeAll
    public static void setup() throws DiceException {
        DICE_ROLLER = new DiceRoller();
    }

    @AfterAll
    public static void tearDown() throws DiceException {
        DiceRoller.cleanUp();
    }

    /**
     * Test that a sum is produced.
     *
     * @throws DiceException
     *             if new dice or roller couldn't be created.
     */
    @Test
    public void rollerTest() throws DiceException {
        final RollAggregator aggregator = new RollAggregator();
        aggregator.addDiceRoll(new DiceRollType(TEST_ID, TEST_NAME, 4, 5, 1));

        DICE_ROLLER.performRolls(aggregator);

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
            new DiceRollType(TEST_ID, TEST_NAME, -1, 2, 1);
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (final DiceException e) {
            assertEquals(expectedErrMsg, e.getMessage());
        }

        try {
            new DiceRollType(TEST_ID, TEST_NAME, 3, 2, 1);
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (final DiceException e) {
            assertEquals(expectedErrMsg, e.getMessage());
        }

        try {
            new DiceRollType(TEST_ID, TEST_NAME, 0, 2, 0);
            fail(EXPECTED_EXCEPTION_NOT_THROWN);
        } catch (final DiceException e) {
            assertEquals(expectedErrMsg, e.getMessage());
        }
    }

    /**
     * Sanity check that the roller is distributing rolls properly.
     *
     * @throws DiceException
     *             there was an unexpected error.
     */
    @Test
    public void frequencyTest() throws DiceException {
        final int numRolls = 10000;
        final int maxResult = 20;

        int testCount = 0;
        do {
            final List<Integer> counts = runAndGetCounts(numRolls, maxResult);

            // Calculate the mean.
            final double mean = numRolls / maxResult;

            // Calculate the standard deviation.
            double variance = 0.0;
            for (int i = 0; i < maxResult; i++) {
                final int count = counts.get(i);
                variance += (count - mean) * (count - mean);
            }
            variance /= maxResult - 1;
            final double stdDev = Math.sqrt(variance);

            final double coeffVar = stdDev / mean;

            testCount++;
            final StringBuilder sb = new StringBuilder();
            sb.append("frequencyTest(): attempt " + testCount + " of " + RETRY_ATTEMPTS + NL);
            sb.append("  Mean   : " + mean + NL);
            sb.append("  SD     : " + stdDev + NL);
            sb.append("  SD/Mean: " + coeffVar + NL + NL);
            System.out.println(sb.toString());

            if (ACCEPTABLE_COEFF_VAR > coeffVar) {
                return;
            }
        } while (testCount < RETRY_ATTEMPTS);

        fail("Retry attempts exceeded");
    }

    private static List<Integer> runAndGetCounts(final int numRolls, final int maxResult) throws DiceException {

        // Roll dice.
        final RollAggregator aggregator = new RollAggregator();
        for (int i = 0; i < numRolls; i++) {
            aggregator.addDiceRoll(new DiceRollType(TEST_ID, TEST_NAME, 1, maxResult, 1));
        }
        DICE_ROLLER.performRolls(aggregator);

        // Compute frequencies.
        final List<Integer> counts = new ArrayList<>(Collections.nCopies(maxResult, 0));
        for (final IDiceRollType roll : aggregator.getDiceRolls()) {
            final int index = roll.getSumResult() - 1;
            final int newCount = counts.get(index) + 1;
            counts.set(index, newCount);
        }

        return counts;
    }
}
