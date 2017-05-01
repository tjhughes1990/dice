package ui.src.main.java.tim.output;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyleConstants;

import process.src.main.java.tim.Request;

public class OutputText {
    private JTextPane textArea;
    private SimpleAttributeSet defaultAttr;
    private SimpleAttributeSet successAttr;
    private SimpleAttributeSet failAttr;
    private StyledDocument doc;

    public OutputText(String defaultText) {
        textArea = new JTextPane();
        textArea.setEditable(false);

        doc = textArea.getStyledDocument();
        defaultAttr = new SimpleAttributeSet();
        failAttr = new SimpleAttributeSet();
        successAttr = new SimpleAttributeSet();

        StyleConstants.setBold(successAttr, true);
        StyleConstants.setForeground(successAttr, new Color(0, 128, 0));

        StyleConstants.setBold(failAttr, true);
        StyleConstants.setForeground(failAttr, new Color(255, 0, 0));

        int defaultTextLines = defaultText.split("\n").length;
        for(int i = defaultTextLines; i <= 10; i++) {
            defaultText += "\n";
        }

        try {
            doc.insertString(0, defaultText, defaultAttr);
        } catch(Exception e) {
            System.out.println("[ERROR]");
        }
    }

    public void populate(Request response) throws BadLocationException {
        List<Integer> rolls = response.getRolls();
        List<Integer> successfulRolls = response.getSuccessfulRolls();

        // Total rolls.
        textArea.setText("");
        doc.insertString(0, "Rolls: [" + rolls.get(0).toString(), defaultAttr);
        for(int i = 1; i < rolls.size(); i++) {
            doc.insertString(doc.getLength(), ", ", defaultAttr);
            if(i % 10 == 0) {
                doc.insertString(doc.getLength(), "\n           ", defaultAttr);
            }
            doc.insertString(doc.getLength(), rolls.get(i).toString(),
                defaultAttr);
        }
        doc.insertString(doc.getLength(),
            "]\nTotal: " + response.getTotal() + "\n\n", defaultAttr);

        // Successes.
        doc.insertString(doc.getLength(), "Successes ("
            + response.getSuccessThreshold() + " or above): ", defaultAttr);
        Integer successes = new Integer(successfulRolls.size());
        if(response.isMaxBoxSelected()) {
            for(int i = 0; i < successfulRolls.size(); i++) {
                if(successfulRolls.get(i).equals(
                        response.getDiceType())) {
                    successes++;
                }
            }
        }
        if(successes == 0) {
            if(response.isOneBoxSelected() && rolls.contains(1)) {
                doc.insertString(doc.getLength(), "BOTCHED", failAttr);
            } else {
                doc.insertString(doc.getLength(), "NONE", failAttr);
            }
            StyleConstants.setBold(defaultAttr, false);
        } else {
            doc.insertString(doc.getLength(), successes.toString(),
                successAttr);

            doc.insertString(doc.getLength(), "\nSuccessful rolls: [",
                defaultAttr);
            if(successfulRolls.size() > 0) {
                doc.insertString(doc.getLength(),
                    successfulRolls.get(0).toString(), defaultAttr);
                for(int i = 1; i < successfulRolls.size(); i++) {
                    doc.insertString(doc.getLength(), ", ", defaultAttr);
                    if(i % 10 == 0) {
                        doc.insertString(doc.getLength(),
                            "\n                            ", defaultAttr);
                    }
                    doc.insertString(doc.getLength(),
                        successfulRolls.get(i).toString(), defaultAttr);
                }
            }
            doc.insertString(doc.getLength(), "]\nSuccess total: "
                + response.getSuccessfulTotal(), defaultAttr);
        }
    }

    public JTextPane getOutputArea() {
        return textArea;
    }
}