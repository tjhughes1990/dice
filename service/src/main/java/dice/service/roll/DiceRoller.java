package dice.service.roll;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import dice.service.common.DiceException;
import dice.service.common.ExtractSharedObject;
import dice.service.types.IDiceRollType;

public class DiceRoller {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");

    /**
     * Constructor.
     *
     * @throws DiceException
     *             if the shared objects were not extracted successfully.
     */
    public DiceRoller() throws DiceException {
        // Load shared objects.
        final Path libDir = Paths.get(System.getProperty("java.io.tmpdir")).toAbsolutePath();

        final String libName = IS_WINDOWS ? "dice.dll" : "libdice.so";
        final Path libPath = libDir.resolve(libName);
        // if (!libPath.toFile().exists()) {
        ExtractSharedObject.extract(libPath);
        // }

        System.load(libPath.toString());
    }

    /**
     * Perform rolls on the aggregated dice.
     *
     * @param rollAggregator
     *            the dice roll aggregator.
     */
    public void performRolls(final RollAggregator rollAggregator) {
        final List<IDiceRollType> diceRollTypes = rollAggregator.getDiceRolls();

        // Convert to array.
        final int size = diceRollTypes.size();
        final IDiceRollType[] diceRollTypeArr = new IDiceRollType[size];
        for (int i = 0; i < size; i++) {
            diceRollTypeArr[i] = diceRollTypes.get(i);
        }

        performRolls(diceRollTypeArr, diceRollTypes.size());
    }

    private synchronized native void performRolls(final IDiceRollType[] diceRollTypes, final int numberOfDice);
}
