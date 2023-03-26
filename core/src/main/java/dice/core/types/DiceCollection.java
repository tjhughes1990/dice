package dice.core.types;

import java.io.Serializable;

/**
 * Dice collection.
 */
public class DiceCollection implements Serializable {

    private static final long serialVersionUID = 5654195725189925247L;

    private String name;
    private Dice dice;
    private int count;

    private transient DiceRolls rolls = DiceRolls.EMPTY;

    public DiceCollection(final String name) {
        this(name, Dice.D6, 1);
    }

    public DiceCollection(final String name, final Dice dice, final int count) {
        this.name = name;
        this.dice = dice;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(final Dice dice) {
        this.dice = dice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    public DiceRolls getRolls() {
        return rolls;
    }

    public void setRolls(final DiceRolls rolls) {
        this.rolls = rolls;
    }

    public String getDescription() {
        return String.format("%d%s", count, dice);
    }
}
