package dice.service.application;

import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dice.common.DiceException;
import dice.common.types.DiceRollCollection;
import dice.common.types.DiceRollType;
import dice.service.roll.DiceRoller;
import dice.service.roll.RollAggregator;

@RestController
public class DiceController {

    private final DiceRoller diceRoller;

    /**
     * Constructor.
     *
     * @throws DiceException
     *             if the {@link DiceRoller} could not be created.
     */
    public DiceController() throws DiceException {
        diceRoller = new DiceRoller();
    }

    /**
     * Perform dice rolls.
     *
     * @param diceRolls
     *            the dice rolls to be performed.
     *
     * @return the dice rolls, with rolls perfomed.
     *
     * @throws DiceException
     *             if the dice definitions were invalid.
     */
    @PostMapping(value = "/roll", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<DiceRollType> roll(@RequestBody final List<DiceRollType> diceRolls) throws DiceException {
        final RollAggregator aggregator = new RollAggregator();
        diceRolls.stream().forEach(d -> aggregator.addDiceRoll(d));
        diceRoller.performRolls(aggregator);

        return diceRolls;
    }

    /**
     * Save a list of dice to the database.
     *
     * @param diceRollCollection
     *            the collection of dice to save to the database.
     *
     * @return {@code true} iff the dice collection was successfully saved to the database.
     */
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean save(@RequestBody final DiceRollCollection diceRollCollection) {
        // TODO
        return false;
    }

    /**
     * Load a collection of dice from the database.
     *
     * @param collectionId
     *            the ID of the collection to load.
     *
     * @return a list of dice in the collection.
     */
    @GetMapping(value = "load")
    public @ResponseBody List<DiceRollType> load(final long collectionId) {
        // TODO
        return Collections.emptyList();
    }
}
