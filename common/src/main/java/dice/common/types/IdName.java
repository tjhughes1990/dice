package dice.common.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ID and a name.
 */
public class IdName {

    private final Long id;
    private final String name;

    @JsonCreator
    public IdName(@JsonProperty(value = "id", required = true) final Long id,
            @JsonProperty(value = "name", required = true) final String name) {

        this.id = id;
        this.name = name;
    }

    /**
     * @return id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @return name.
     */
    public String getName() {
        return name;
    }
}
