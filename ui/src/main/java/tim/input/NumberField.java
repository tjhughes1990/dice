package ui.src.main.java.tim.input;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import ui.src.main.java.tim.input.InputField;
import ui.src.main.java.tim.input.IntFilter;

public class NumberField extends BaseField {
    private JTextField field;

    private int min = 0;
    private int max = 20;

    private static final int INPUT_BOX_SIZE = 3;

    public NumberField(String labelName) {
        setLabel(new JLabel(labelName + ":"));
        field = new JTextField(INPUT_BOX_SIZE);
        setField(field);

        setLabelPanel(new JPanel(new FlowLayout(FlowLayout.TRAILING)));
        setFieldPanel(new JPanel(new FlowLayout(FlowLayout.LEADING)));

        addComponentsToPanels();

        // Validate on input.
        PlainDocument doc = (PlainDocument) field.getDocument();
        doc.setDocumentFilter(new IntFilter());
    }

    public void addDocumentListener(DocumentListener documentListener) {
        field.getDocument().addDocumentListener(documentListener);
    }

    // Getters for public access.

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    @Override
    public Integer getValue() {
        try {
            return Integer.parseInt(field.getText());
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public void setValue(Object value) {
        field.setText((String)value);
    }

    @Override
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
