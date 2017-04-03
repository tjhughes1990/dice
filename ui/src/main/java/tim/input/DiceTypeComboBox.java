package ui.src.main.java.tim.input;

import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DiceTypeComboBox {
    private JComboBox box;
    private JLabel label;

    private DiceTypeMgr diceTypeMgr;

    public DiceTypeComboBox(String labelName) {
        diceTypeMgr = new DiceTypeMgr();

        label = new JLabel(labelName + ":");
        box = new JComboBox(getNamesArray());
    }

    private String[] getNamesArray() {
        final List<DiceType> diceTypeList = diceTypeMgr.getDiceTypeList();
        String[] diceNameList = new String[diceTypeList.size()];
        for(int i = 0; i < diceTypeList.size(); i++) {
            diceNameList[i] = diceTypeList.get(i).getDiceName();
        }
        return diceNameList;
    }

    public JComboBox getComboBox() {
        return box;
    }

    public JPanel getBoxPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panel.add(box);
        return panel;
    }

    public JPanel getLabelPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        panel.add(label);
        return panel;
    }

    public DiceType getSelected() {
        final int ind = box.getSelectedIndex();
        return diceTypeMgr.getDiceTypeList().get(ind);
    }
}