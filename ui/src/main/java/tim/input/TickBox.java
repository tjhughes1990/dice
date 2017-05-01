package ui.src.main.java.tim.input;

import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TickBox {
    private JCheckBox checkBox;
    private JLabel label;

    private JPanel labelPanel;
    private JPanel checkBoxPanel;

    public TickBox(String labelName) {
        label = new JLabel(labelName + ":");
        checkBox = new JCheckBox();
        checkBox.setSelected(false);

        labelPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        labelPanel.add(label);
        checkBoxPanel.add(checkBox);
    }

    public boolean isSelected() {
        return checkBox.isSelected();
    }

    public JPanel getLabelPanel() {
        return labelPanel;
    }

    public JPanel getCheckBoxPanel() {
        return checkBoxPanel;
    }

    public void setSelected(boolean value) {
        checkBox.setSelected(value);
    }
}