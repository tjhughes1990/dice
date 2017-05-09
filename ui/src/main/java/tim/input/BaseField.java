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
    public void setField(final JComponent field) {
        this.field = field;
    }

    @Override
    public void setLabel(final JLabel label) {
        this.label = label;
    }

    @Override
    public JPanel getFieldPanel() {
        return fieldPanel;
    }

    @Override
    public void setFieldPanel(final JPanel fieldPanel) {
        this.fieldPanel = fieldPanel;
    }

    @Override
    public JPanel getLabelPanel() {
        return labelPanel;
    }

    @Override
    public void setLabelPanel(final JPanel labelPanel) {
        this.labelPanel = labelPanel;
    }

    @Override
    public void addComponentsToPanels() {
        labelPanel.add(label);
        fieldPanel.add(field);
    }

    @Override
    public void setFont(final Font labelFont, final Font fieldFont) {
        label.setFont(labelFont);
        field.setFont(fieldFont);
    }

    @Override
    public abstract <T> T getValue();

    @Override
    public abstract void setValue(final Object value);
}