package dice.core;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dice.core.types.DiceCollection;
import dice.core.types.DiceRolls;

/**
 * Dice roller.
 */
public class DiceRoller {

    private final Random rand;
    private final List<DiceCollection> diceCollections;

    /**
     * Constructor.
     *
     * @param seed
     * @param diceCollections
     */
    public DiceRoller(final long seed, final List<DiceCollection> diceCollections) {
        rand = new Random(seed);
        this.diceCollections = diceCollections;
    }

    /**
     * Constructor.
     *
     * @param diceCollections
     */
    public DiceRoller(final List<DiceCollection> diceCollections) {
        this(Instant.now().getEpochSecond(), diceCollections);
    }

    public void performRoll() {
        for (final DiceCollection diceCollection : diceCollections) {
            final int count = diceCollection.getCount();
            final int sides = diceCollection.getDice().sides();

            final List<Integer> values = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                values.add(rand.nextInt(sides) + 1);
            }

            diceCollection.setRolls(DiceRolls.fromValues(values));
        }
    }
}
