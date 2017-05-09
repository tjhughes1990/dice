package ui.src.main.java.tim.input;

public class DiceType {
    private String diceId;
    private String diceName;
    private Integer faces;

    public DiceType(final String diceId, final String diceName,
        final Integer faces) {
        setDiceId(diceId);
        setDiceName(diceName);
        setFaces(faces);
    }

    public DiceType() {
        this(null, null, null);
    }

    // Getters and setters.
    public String getDiceId() {
        return diceId;
    }

    public void setDiceId(final String diceId) {
        this.diceId = diceId;
    }

    public String getDiceName() {
        return diceName;
    }

    public void setDiceName(final String diceName) {
        this.diceName = diceName;
    }

    public Integer getFaces() {
        return faces;
    }

    public void setFaces(final Integer faces) {
        this.faces = faces;
    }
}