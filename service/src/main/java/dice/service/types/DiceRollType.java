package dice.service.types;

/**
 * Default dice roll type.
 */
public class DiceRollType extends AbstractDiceRollType {

    /**
     * Constructor.
     *
     * @param minResult
     *            the min result.
     * @param maxResult
     *            the max result.
     * @param rollNumber
     *            the number of rolls.
     */
    private DiceRollType(final int minResult, final int maxResult, final int rollNumber) {
        super(minResult, maxResult, rollNumber);
    }

    /**
     * Create a default dice.
     *
     * @param diceType
     *            a default dice type.
     * @param rollNumber
     *            the number of rolls.
     * @return A default dice roll type.
     */
    public static IDiceRollType create(final DiceType diceType, final int rollNumber) {
        return new DiceRollType(diceType.getMinResult(), diceType.getMaxResult(), rollNumber);
    }
}
