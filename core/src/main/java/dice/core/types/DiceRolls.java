package dice.core.types;

import java.util.Collections;
import java.util.List;

public record DiceRolls(
    List<Integer> values,
    int total,
    int min,
    int max
) {

    public static final DiceRolls EMPTY = DiceRolls.fromValues(Collections.emptyList());

    public static DiceRolls fromValues(final List<Integer> values) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int total = 0;
        for (final int value : values) {
            min = Math.min(min, value);
            max = Math.max(max, value);
            total += value;
        }

        return new DiceRolls(values, total, min, max);
    }
}
