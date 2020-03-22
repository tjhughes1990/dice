package dice.common.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import dice.common.DiceException;

/**
 * Abstract dice type pojo.
 */
@JsonInclude(Include.NON_NULL)
public class DiceRollType extends IdName implements IDiceRollType {

    private final int minResult;
    private final int maxResult;
    private final int rollNumber;

    // Can be set from the native C++ code via JNI.
    private volatile Integer sumResult;

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
        this(null, null, minResult, maxResult, rollNumber);
    }

    /**
     * Constructor.
     *
     * @param id
     *            the dice id.
     * @param name
     *            the dice name.
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
    public DiceRollType(@JsonProperty(value = "id", required = true) final Long id,
            @JsonProperty(value = "name", required = true) final String name,
            @JsonProperty(value = "minResult", required = true) final int minResult,
            @JsonProperty(value = "maxResult", required = true) final int maxResult,
            @JsonProperty(value = "rollNumber", required = true) final int rollNumber) throws DiceException {

        super(id, name);
        if (minResult < 0 || minResult >= maxResult || rollNumber <= 0) {
            throw new DiceException("Invalid dice configuration specified");
        }

        this.minResult = minResult;
        this.maxResult = maxResult;
        this.rollNumber = rollNumber;
        sumResult = null;
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
    public int getRollNumber() {
        return rollNumber;
    }

    @Override
    public Integer getSumResult() {
        return sumResult;
    }

    @Override
    public void setSumResult(final int sumResult) {
        this.sumResult = sumResult == -1 ? null : sumResult;
    }
}
