package dice.common.types;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Dice roll type interface.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = DiceRollType.class, name = "DiceRollType") })
public interface IDiceRollType {

    /**
     * @return the minimum roll result.
     */
    int getMinResult();

    /**
     * @return the maximum roll result.
     */
    int getMaxResult();

    /**
     * @return the number of rolls to perform.
     */
    int getRollNumber();

    /**
     * @return the sum roll result;
     */
    Integer getSumResult();

    /**
     * @param sum
     *            the sum result to set.
     */
    void setSumResult(final int sum);
}
