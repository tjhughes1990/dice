package dice.common.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Id name extension with a list of dice types.
 */
public class DiceRollCollection extends IdName {

    private final List<DiceRollType> diceRolls;

    /**
     * Constructor.
     *
     * @param id
     *            the collection ID.
     * @param name
     *            the collection name.
     * @param diceRolls
     *            the collection of dice rolls.
     */
    public DiceRollCollection(@JsonProperty(value = "id", required = true) final long id,
            @JsonProperty(value = "name", required = true) final String name,
            @JsonProperty(value = "diceRolls", required = true) final List<DiceRollType> diceRolls) {

        super(id, name);
        this.diceRolls = diceRolls;
    }

    /**
     * @return diceRolls.
     */
    public List<DiceRollType> getDiceRolls() {
        return diceRolls;
    }
}
