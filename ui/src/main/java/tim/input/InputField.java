package ui.src.main.java.tim.input;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.PlainDocument;

import ui.src.main.java.tim.input.IntFilter;


public class InputField {
    private JLabel label;
    private JTextField field;

    private int min = 0;
    private int max = 20;

    private static final int INPUT_BOX_SIZE = 3;

    public InputField(String labelName) {
        label = new JLabel(labelName + ":");
        field = new JTextField(INPUT_BOX_SIZE);

        // Validate on input.
        PlainDocument doc = (PlainDocument) field.getDocument();
        doc.setDocumentFilter(new IntFilter());
    }

    // Getters for public access.

    public JTextField getField() {
        return field;
    }

    public JPanel getFieldPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panel.add(field);
        return panel;
    }

    public JPanel getLabelPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        panel.add(label);
        return panel;
    }

    public Integer getValue() {
        try {
            return Integer.parseInt(field.getText());
        } catch(Exception e) {
            return null;
        }
    }

    public void setValue(String value) {
        field.setText(value);
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public boolean isValid() {
        if(getValue() == null) {
            return false;
        }
        else if(getValue() <= min || getValue() > max) {
            return false;
        }
        return true;
    }
}
