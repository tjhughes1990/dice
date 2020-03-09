package dice.common.types;

import dice.common.DiceException;

/**
 * Abstract dice type pojo.
 */
public class DiceRollType implements IDiceRollType {

    private int minResult;
    private int maxResult;
    private int rollNumber;

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
     *
     * @throws DiceException
     *             if the supplied arguments were invalid.
     */
    public DiceRollType(final int minResult, final int maxResult, final int rollNumber) throws DiceException {
        if (minResult < 0 || minResult >= maxResult || rollNumber <= 0) {
            throw new DiceException("Invalid dice configuration specified");
        }

        this.minResult = minResult;
        this.maxResult = maxResult;
        this.rollNumber = rollNumber;
    }

    /**
     * Empty constructor used by Spring.
     */
    public DiceRollType() {
    }

    @Override
    public int getMinResult() {
        return minResult;
    }

    /**
     * @param minResult
     *            the min result to set.
     */
    public void setMinResult(final int minResult) {
        this.minResult = minResult;
    }

    @Override
    public int getMaxResult() {
        return maxResult;
    }

    /**
     * @param maxResult
     *            the max result to set.
     */
    public void setMaxResult(final int maxResult) {
        this.maxResult = maxResult;
    }

    @Override
    public int getRollNumber() {
        return rollNumber;
    }

    /**
     * @param rollNumber
     *            the roll number to set.
     */
    public void setRollNumber(final int rollNumber) {
        this.rollNumber = rollNumber;
    }

    @Override
    public Integer getSumResult() {
        return sumResult == -1 ? null : sumResult;
    }

    @Override
    public void setSumResult(final int sumResult) {
        this.sumResult = sumResult;
    }
}
