package ui.src.main.java.tim.input;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public interface InputField {
    public JComponent getField();
    public void setField(JComponent field);
    public void setLabel(JLabel label);

    public JPanel getFieldPanel();
    public void setFieldPanel(JPanel fieldPanel);
    public JPanel getLabelPanel();
    public void setLabelPanel(JPanel labelPanel);

    public void addComponentsToPanels();
    public <T> T getValue();
    public void setValue(Object value);
    public boolean isValid();
}