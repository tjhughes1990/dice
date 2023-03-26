package dice.core.io;

import java.io.File;
import java.util.List;
import java.util.Optional;

import dice.core.types.DiceCollection;

public interface DiceCollectionSerialiser {

    /**
     * Serialise a collection of {@link DiceCollection} instances.
     *
     * @param file               destination file.
     * @param diceCollectionList dice collections to serialise.
     *
     * @return true iff serialisation was successful.
     */
    boolean serialise(File file, List<DiceCollection> diceCollectionList);

    /**
     * @param file the source file.
     *
     * @return a collection of deserialised {@link DiceCollection} instances.
     */
    Optional<List<DiceCollection>> deserialise(File file);
}
