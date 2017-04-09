package process.src.main.java.tim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Request {

    private final List<Integer> rolls = new ArrayList<Integer>();
    private final List<Integer> successfulRolls = new ArrayList<Integer>();
    private final int diceType;
    private final int successThreshold;
    private final boolean tenBox;
    private final boolean oneBox;

    private Integer total;
    private Integer successfulTotal;

    public Request(int type, int num, int threshold, boolean tenBox,
            boolean oneBox) {
        total = 0;
        successfulTotal = 0;
        for(int i = 0; i < num; i++) {
            Integer newRoll = getRoll(type);
            rolls.add(newRoll);
            total += newRoll;
            if(newRoll >= threshold) {
                successfulRolls.add(newRoll);
                successfulTotal += newRoll;
            }
        }

        diceType = type;
        successThreshold = threshold;
        this.tenBox = tenBox;
        this.oneBox = oneBox;
    }

    public List<Integer> getRolls() {
        return rolls;
    }

    public List<Integer> getSuccessfulRolls() {
        return successfulRolls;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getSuccessfulTotal() {
        return successfulTotal;
    }

    public int numberOfSuccesses() {
        return successfulRolls.size();
    }

    public boolean isTenBoxSelected() {
        return tenBox;
    }

    public boolean isOneBoxSelected() {
        return oneBox;
    }

    public int getDiceType() {
        return diceType;
    }

    public int getSuccessThreshold() {
        return successThreshold;
    }

    private Integer getRoll(int diceMax) {
        Random rand = new Random();
        return new Integer(rand.nextInt(diceMax) + 1);
    }
}
