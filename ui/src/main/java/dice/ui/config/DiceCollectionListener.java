package dice.ui.config;

import dice.core.types.DiceCollection;

/**
 * Listener for changes to selected dice collection.
 */
interface DiceCollectionListener {

    /**
     * @param diceCollection the new {@link DiceCollection}.
     */
    void update(DiceCollection diceCollection);
}
