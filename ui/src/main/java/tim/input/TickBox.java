package ui.src.main.java.tim.input;

import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TickBox extends BaseField {
    private JCheckBox field;

    public TickBox(String labelName) {
        setLabel(new JLabel(labelName + ":"));
        field = new JCheckBox();
        setField(field);

        setLabelPanel(new JPanel(new FlowLayout(FlowLayout.TRAILING)));
        setFieldPanel(new JPanel(new FlowLayout(FlowLayout.LEADING)));

        addComponentsToPanels();
    }

    @Override
    public Boolean getValue() {
        return field.isSelected();
    }

    @Override
    public void setValue(Object value) {
        field.setSelected((Boolean)value);
    }
}