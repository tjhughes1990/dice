package dice.service.types;

/**
 * Dice roll type interface.
 */
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
    int getNumberOfRolls();

    /**
     * @return the sum roll result;
     */
    Integer getSumResult();
}
