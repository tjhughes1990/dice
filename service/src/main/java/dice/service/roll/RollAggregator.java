package dice.service.roll;

import java.util.ArrayList;
import java.util.List;

import dice.service.types.DiceRollType;
import dice.service.types.DiceType;
import dice.service.types.IDiceRollType;

/**
 * Roll aggregator.
 */
public class RollAggregator {

    private final List<IDiceRollType> diceRolls = new ArrayList<>();

    /**
     * Add a default dice to the rolls.
     *
     * @param diceType
     *            the dice type to add.
     * @param rollNumber
     *            the number of rolls.
     */
    public void addDefaultDiceRoll(final DiceType diceType, final int rollNumber) {
        diceRolls.add(DiceRollType.create(diceType, rollNumber));
    }

    /**
     * @return a list of dice rolls to perform.
     */
    public List<IDiceRollType> getDiceRolls() {
        return diceRolls;
    }
}
