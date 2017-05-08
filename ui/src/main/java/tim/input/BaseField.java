package ui.src.main.java.tim.input;

import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class BaseField implements InputField {
    private JLabel label;
    private JComponent field;

    private JPanel labelPanel;
    private JPanel fieldPanel;

    public BaseField() {
    }

    // Implement InputField
    @Override
    public JComponent getField() {
        return field;
    }

    @Override
    public void setField(JComponent field) {
        this.field = field;
    }

    @Override
    public void setLabel(JLabel label) {
        this.label = label;
    }

    @Override
    public JPanel getFieldPanel() {
        return fieldPanel;
    }

    @Override
    public void setFieldPanel(JPanel fieldPanel) {
        this.fieldPanel = fieldPanel;
    }

    @Override
    public JPanel getLabelPanel() {
        return labelPanel;
    }

    @Override
    public void setLabelPanel(JPanel labelPanel) {
        this.labelPanel = labelPanel;
    }

    @Override
    public void addComponentsToPanels() {
        labelPanel.add(label);
        fieldPanel.add(field);
    }

    @Override
    public void setFont(Font labelFont, Font fieldFont) {
        label.setFont(labelFont);
        field.setFont(fieldFont);
    }

    @Override
    public abstract <T> T getValue();

    @Override
    public abstract void setValue(Object value);

    @Override
    public abstract boolean isValid();
}