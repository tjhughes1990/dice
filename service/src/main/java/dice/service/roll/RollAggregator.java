package dice.service.roll;

import java.util.ArrayList;
import java.util.List;

import dice.common.types.IDiceRollType;

/**
 * Roll aggregator.
 */
public class RollAggregator {

    private final List<IDiceRollType> diceRolls = new ArrayList<>();

    /**
     * Add a default dice to the rolls.
     *
     * @param diceRollType
     *            the dice type to add.
     */
    public void addDiceRoll(final IDiceRollType diceRollType) {
        diceRolls.add(diceRollType);
    }

    /**
     * @return a list of dice rolls to perform.
     */
    public List<IDiceRollType> getDiceRolls() {
        return diceRolls;
    }
}
