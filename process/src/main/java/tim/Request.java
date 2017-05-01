package process.src.main.java.tim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Request {

    private final List<Integer> rolls = new ArrayList<Integer>();
    private final List<Integer> successfulRolls = new ArrayList<Integer>();
    private final Integer diceType;
    private final Integer successThreshold;
    private final boolean maxBox;
    private final boolean oneBox;

    private Integer total;
    private Integer successfulTotal;

    public Request(int type, Integer num, Integer threshold, boolean maxBox,
            boolean oneBox) {
        total = 0;
        successfulTotal = 0;
        for(Integer i = 0; i < num; i++) {
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
        this.maxBox = maxBox;
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

    public Integer numberOfSuccesses() {
        return successfulRolls.size();
    }

    public boolean isMaxBoxSelected() {
        return maxBox;
    }

    public boolean isOneBoxSelected() {
        return oneBox;
    }

    public Integer getDiceType() {
        return diceType;
    }

    public Integer getSuccessThreshold() {
        return successThreshold;
    }

    private Integer getRoll(int diceMax) {
        Random rand = new Random();
        return new Integer(rand.nextInt(diceMax) + 1);
    }
}
