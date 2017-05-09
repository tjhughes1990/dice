package ui.src.main.java.tim.input;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DiceTypeComboBox extends BaseField {
    private JComboBox box;

    private DiceTypeMgr diceTypeMgr;

    public DiceTypeComboBox(final String labelName) {
        diceTypeMgr = new DiceTypeMgr();

        setLabel(new JLabel(labelName + ":"));
        box = new JComboBox(getNamesArray());
        setField(box);

        setLabelPanel(new JPanel(new FlowLayout(FlowLayout.TRAILING)));
        setFieldPanel(new JPanel(new FlowLayout(FlowLayout.LEADING)));

        addComponentsToPanels();
    }

    public void addActionListener(final ActionListener actionListener) {
        box.addActionListener(actionListener);
    }

    private String[] getNamesArray() {
        final List<DiceType> diceTypeList = diceTypeMgr.getDiceTypeList();
        final String[] diceNameList = new String[diceTypeList.size()];
        for(int i = 0; i < diceTypeList.size(); i++) {
            diceNameList[i] = diceTypeList.get(i).getDiceName();
        }
        return diceNameList;
    }

    @Override
    public DiceType getValue() {
        final int ind = box.getSelectedIndex();
        return diceTypeMgr.getDiceTypeList().get(ind);
    }

    @Override
    public void setValue(final Object id) {
        final List<DiceType> diceTypeList = diceTypeMgr.getDiceTypeList();
        for(int i = 0; i < diceTypeList.size(); i++) {
            if(diceTypeList.get(i).getDiceId().equals((String)id)) {
                box.setSelectedIndex(i);
                return;
            }
        }
    }
}