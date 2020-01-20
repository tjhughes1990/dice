package dice.service.types;

/**
 * Default dice types.
 */
public enum DiceType {
    COIN(0, 1), D4(1, 4), D6(1, 6), D8(1, 8), D10(1, 10), D12(1, 12), D20(1, 20);

    private final int minResult;
    private final int maxResult;

    /**
     * Constructor.
     *
     * @param minResult
     *            the min result.
     * @param maxResult
     *            the max result.
     */
    private DiceType(final int minResult, final int maxResult) {
        this.minResult = minResult;
        this.maxResult = maxResult;
    }

    /**
     * @return the min result.
     */
    public int getMinResult() {
        return minResult;
    }

    /**
     * @return the max result.
     */
    public int getMaxResult() {
        return maxResult;
    }
}
