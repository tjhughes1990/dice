package ui.src.main.java.tim.input;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class IntFilter extends DocumentFilter {

    private boolean valid = false;

    @Override
    public void insertString(final FilterBypass fb, final int offset,
            final String str, final AttributeSet attr)
            throws BadLocationException {
        final Document doc = fb.getDocument();
        final StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, str);

        if (validate(sb.toString())) {
           super.insertString(fb, offset, str, attr);
        }
    }

    @Override
    public void replace(final FilterBypass fb, final int offset,
            final int length, final String str, final AttributeSet attrs)
            throws BadLocationException {
        final Document doc = fb.getDocument();
        final StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, str);

        if (validate(sb.toString())) {
           super.replace(fb, offset, length, str, attrs);
        }
    }

    @Override
    public void remove(final FilterBypass fb, final int offset,
            final int length) throws BadLocationException {
        final Document doc = fb.getDocument();
        final StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if (validate(sb.toString())) {
           super.remove(fb, offset, length);
        }
    }

    public boolean validate(final String str) {
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

    private void setValid(final boolean valid) {
        this.valid = valid;
    }
}