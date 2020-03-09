package dice.database.application;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import dice.common.types.DiceRollType;

@RestController
public class DatabaseController {

    /**
     * Constructor.
     */
    public DatabaseController() {
    }

    @PostMapping
    public void saveDice(final List<DiceRollType> diceList) {
        // TODO
    }

    @GetMapping
    public List<DiceRollType> loadDice() {
        // TODO
        return Collections.emptyList();
    }
}
