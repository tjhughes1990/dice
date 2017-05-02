package ui.src.main.java.tim.input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.src.main.java.tim.input.DiceType;

public class DiceTypeMgr {

    private static final String DT_ID_D4 = "DT_D4";
    private static final String DT_ID_D6 = "DT_D6";
    private static final String DT_ID_D8 = "DT_D8";
    private static final String DT_ID_D10 = "DT_D10";
    private static final String DT_ID_D12 = "DT_D12";
    private static final String DT_ID_D20 = "DT_D20";

    private static final String DT_NAME_D4 = "D4";
    private static final String DT_NAME_D6 = "D6";
    private static final String DT_NAME_D8 = "D8";
    private static final String DT_NAME_D10 = "D10";
    private static final String DT_NAME_D12 = "D12";
    private static final String DT_NAME_D20 = "D20";

    private static final Integer DT_FACES_D4 = new Integer(4);
    private static final Integer DT_FACES_D6 = new Integer(6);
    private static final Integer DT_FACES_D8 = new Integer(8);
    private static final Integer DT_FACES_D10 = new Integer(10);
    private static final Integer DT_FACES_D12 = new Integer(12);
    private static final Integer DT_FACES_D20 = new Integer(20);

    private final List<DiceType> diceTypeList = new ArrayList<DiceType>() {{
        add(new DiceType(DT_ID_D4, DT_NAME_D4, 4));
        add(new DiceType(DT_ID_D6, DT_NAME_D6, 6));
        add(new DiceType(DT_ID_D8, DT_NAME_D8, 8));
        add(new DiceType(DT_ID_D10, DT_NAME_D10, 10));
        add(new DiceType(DT_ID_D12, DT_NAME_D12, 12));
        add(new DiceType(DT_ID_D20, DT_NAME_D20, 20));
    }};

    public List<DiceType> getDiceTypeList() {
        return diceTypeList;
    }
}