package dice.service.roller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import dice.service.common.DiceException;
import dice.service.roll.DiceRoller;
import dice.service.roll.RollAggregator;
import dice.service.types.DiceType;
import dice.service.types.IDiceRollType;

/**
 * Tests the roller.
 */
public class RollerTest {

    @Test
    public void rollerTest() throws DiceException {
        final RollAggregator aggregator = new RollAggregator();
        aggregator.addDefaultDiceRoll(DiceType.COIN, 1);

        final DiceRoller roller = new DiceRoller();
        roller.performRolls(aggregator);

        final List<IDiceRollType> diceRolls = aggregator.getDiceRolls();
        assertEquals(1, diceRolls.size());
        final int sum = diceRolls.get(0).getSumResult();
        assertTrue(sum == 0 || sum == 1);
    }
}
