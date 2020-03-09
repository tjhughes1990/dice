package dice.common.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Collection of dice with a name.
 */
public class DiceRollCollection {

    private final long id;
    private final String collectionName;
    private final List<IDiceRollType> diceRollTypes;

    @JsonCreator
    public DiceRollCollection(@JsonProperty(value = "id", required = true) final long id,
            @JsonProperty(value = "collection_name", required = true) final String collectionName,
            @JsonProperty(value = "diceRollTypes", required = true) final List<IDiceRollType> diceRollTypes) {

        this.id = id;
        this.collectionName = collectionName;
        this.diceRollTypes = diceRollTypes;
    }

    /**
     * @return id.
     */
    public long getId() {
        return id;
    }

    /**
     * @return diceRollTypes.
     */
    public List<IDiceRollType> getDiceRollTypes() {
        return diceRollTypes;
    }

    /**
     * @return collectionName.
     */
    public String getName() {
        return collectionName;
    }
}
