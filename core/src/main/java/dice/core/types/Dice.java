package dice.core.types;

/**
 * Dice.
 *
 * @param sides
 */
public record Dice(
    int sides
) {

    /**
     * Constructor.
     *
     * @param diceType
     */
    public Dice(final DiceType diceType) {
        this(diceType.getSides());
    }
}
