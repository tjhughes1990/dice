package ui.src.main.java.tim.input;

public class DiceType {
    private String diceId;
    private String diceName;
    private Integer faces;

    public DiceType(String diceId, String diceName, Integer faces) {
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

    public void setDiceId(String diceId) {
        this.diceId = diceId;
    }

    public String getDiceName() {
        return diceName;
    }

    public void setDiceName(String diceName) {
        this.diceName = diceName;
    }

    public Integer getFaces() {
        return faces;
    }

    public void setFaces(Integer faces) {
        this.faces = faces;
    }
}