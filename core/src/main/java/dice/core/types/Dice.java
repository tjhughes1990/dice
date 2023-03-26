package dice.core.types;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Dice.
 *
 * @param sides
 */
public record Dice(
    int sides
) implements Comparable<Dice>, Serializable {

    @Override
    public String toString() {
        return String.format("D%d", sides);
    }

    public static final Dice D2 = new Dice(2);

    public static final Dice D4 = new Dice(4);
    public static final Dice D6 = new Dice(6);
    public static final Dice D8 = new Dice(8);
    public static final Dice D10 = new Dice(10);
    public static final Dice D12 = new Dice(12);
    public static final Dice D20 = new Dice(20);
    public static final Dice D100 = new Dice(100);

    public static final SortedSet<Dice> DEFAULT_DICE = new TreeSet<>(Set.of(D2, D4, D6, D8, D10, D12, D20, D100));
    public static final Dice DEFAULT_DIE = D6;

    @Override
    public int compareTo(final Dice other) {
        return Integer.compare(sides, other.sides());
    }
}
