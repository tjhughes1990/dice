package dice.service.types;

/**
 * Default dice roll type.
 */
public class DefaultDiceRollType extends AbstractDiceRollType {

    /**
     * Constructor.
     *
     * @param maxResult
     *            the max result.
     * @param rollNumber
     *            the number of rolls.
     */
    private DefaultDiceRollType(final int maxResult, final int rollNumber) {
        super(1, maxResult, rollNumber);
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
        return new DefaultDiceRollType(diceType.getMaxResult(), rollNumber);
    }
}
