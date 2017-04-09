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

    private JPanel labelPanel;
    private JPanel fieldPanel;

    private int min = 0;
    private int max = 20;

    private static final int INPUT_BOX_SIZE = 3;

    public InputField(String labelName) {
        label = new JLabel(labelName + ":");
        field = new JTextField(INPUT_BOX_SIZE);

        labelPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        // Validate on input.
        PlainDocument doc = (PlainDocument) field.getDocument();
        doc.setDocumentFilter(new IntFilter());

        labelPanel.add(label);
        fieldPanel.add(field);
    }

    // Getters for public access.

    public JTextField getField() {
        return field;
    }

    public JPanel getFieldPanel() {
        return fieldPanel;
    }

    public JPanel getLabelPanel() {
            return labelPanel;
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
