package dice.service.types;

/**
 * Default dice types.
 */
public enum DiceType {
    COIN(2), D4(4), D6(6), D8(8), D10(10), D12(12), D20(20);

    private final int maxResult;

    /**
     * Constructor.
     *
     * @param maxResult
     *            the max result.
     */
    private DiceType(final int maxResult) {
        this.maxResult = maxResult;
    }

    /**
     * @return the max result.
     */
    public int getMaxResult() {
        return maxResult;
    }
}
