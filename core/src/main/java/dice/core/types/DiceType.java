package dice.core.types;

/**
 * Dice DTO
 */
public enum DiceType {
    D2(2),
    D4(4),
    D6(6),
    D8(8),
    D10(10),
    D12(12),
    D20(20),
    D100(100);

    private final int sides;

    DiceType(final int sides) {
        this.sides = sides;
    }

    /**
     * @return the sides;
     */
    public int getSides() {
        return sides;
    }
}