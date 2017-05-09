package ui.src.main.java.tim.input;

import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public interface InputField {
    public JComponent getField();
    public void setField(final JComponent field);
    public void setLabel(final JLabel label);

    public JPanel getFieldPanel();
    public void setFieldPanel(final JPanel fieldPanel);
    public JPanel getLabelPanel();
    public void setLabelPanel(final JPanel labelPanel);

    public void setFont(final Font labelFont, final Font fieldFont);

    public void addComponentsToPanels();
    public <T> T getValue();
    public void setValue(final Object value);
}