package process.src.main.java.tim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Request {

    private List<Integer> rolls = new ArrayList<Integer>();
    private List<Integer> successfulRolls = new ArrayList<Integer>();
    private Integer total;
    private Integer successfulTotal;

    public Request(int type, int num, int threshold) {
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

    private Integer getRoll(int diceMax) {
        Random rand = new Random();
        return new Integer(rand.nextInt(diceMax) + 1);
    }
}
