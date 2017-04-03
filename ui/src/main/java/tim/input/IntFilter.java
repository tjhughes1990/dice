package ui.src.main.java.tim.input;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class IntFilter extends DocumentFilter {

    private boolean valid = false;

    @Override
    public void insertString(FilterBypass fb, int offset, String str,
            AttributeSet attr) throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, str);

        if (validate(sb.toString())) {
           super.insertString(fb, offset, str, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String str,
            AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, str);

        if (validate(sb.toString())) {
           super.replace(fb, offset, length, str, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length)
            throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if (validate(sb.toString())) {
           super.remove(fb, offset, length);
        }
    }

    public boolean validate(String str) {
        if(str.length() == 0) {
            return true;
        }
        try {
            Integer.parseInt(str);
        } catch(Exception e) {
            return false;
        }
        return true;
    }

    public boolean getValid() {
        return valid;
    }

    private void setValid(boolean valid) {
        this.valid = valid;
    }
}