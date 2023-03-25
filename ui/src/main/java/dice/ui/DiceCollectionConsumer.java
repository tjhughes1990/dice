package dice.ui;

import java.io.IOException;
import java.util.List;

import dice.core.types.DiceCollection;

/**
 * Dice collection consumer.
 */
public interface DiceCollectionConsumer extends ThrowingConsumer<List<DiceCollection>, IOException> {
}
