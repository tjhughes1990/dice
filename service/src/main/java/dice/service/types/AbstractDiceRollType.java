package dice.service.types;

/**
 * Abstract dice type pojo.
 */
public abstract class AbstractDiceRollType implements IDiceRollType {

    private final int minResult;
    private final int maxResult;
    private final int rollNumber;

    private volatile int sumResult = -1;

    /**
     * Constructor.
     *
     * @param minResult
     *            the min dice result.
     * @param maxResult
     *            the max dice result.
     * @param rollNumber
     *            the number of rolls.
     */
    AbstractDiceRollType(final int minResult, final int maxResult, final int rollNumber) {
        this.minResult = minResult;
        this.maxResult = maxResult;
        this.rollNumber = rollNumber;
    }

    @Override
    public int getMinResult() {
        return minResult;
    }

    @Override
    public int getMaxResult() {
        return maxResult;
    }

    @Override
    public int getNumberOfRolls() {
        return rollNumber;
    }

    @Override
    public Integer getSumResult() {
        return sumResult == -1 ? null : sumResult;
    }
}
