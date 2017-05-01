package ui.src.main.java.tim.input;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DiceTypeComboBox {
    private JComboBox box;
    private JLabel label;

    private JPanel labelPanel;
    private JPanel comboBoxPanel;

    private DiceTypeMgr diceTypeMgr;

    public DiceTypeComboBox(String labelName) {
        diceTypeMgr = new DiceTypeMgr();

        label = new JLabel(labelName + ":");
        box = new JComboBox(getNamesArray());

        labelPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        labelPanel.add(label);
        comboBoxPanel.add(box);
    }

    public void addActionListener(ActionListener actionListener) {
        box.addActionListener(actionListener);
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
        return comboBoxPanel;
    }

    public JPanel getLabelPanel() {
        return labelPanel;
    }

    public DiceType getSelected() {
        final int ind = box.getSelectedIndex();
        return diceTypeMgr.getDiceTypeList().get(ind);
    }

    public void setSelected(final String id) {
        final List<DiceType> diceTypeList = diceTypeMgr.getDiceTypeList();
        for(int i = 0; i < diceTypeList.size(); i++) {
            if(diceTypeList.get(i).getDiceId().equals(id)) {
                box.setSelectedIndex(i);
                return;
            }
        }
    }
}