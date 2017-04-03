package ui.src.main.java.tim;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ui.src.main.java.tim.input.DiceType;
import ui.src.main.java.tim.input.DiceTypeComboBox;
import ui.src.main.java.tim.input.InputField;
import process.src.main.java.tim.Request;

public class Window implements ActionListener, DocumentListener {
    private JFrame jFrame;
    private JPanel container;
    private JPanel inputContainer;
    private JPanel rollContainer;
    private JPanel titleContainer;
    private JPanel responseContainer;

    private static JLabel title;
    private DiceTypeComboBox diceTypeComboBox;
    private InputField diceNumber;
    private InputField successThreshold;
    private JButton rollButton;
    private JTextArea textArea;

    private Request response;

    private static final String DICE_TYPE_LABEL = "Dice Type";
    private static final String DICE_NUM_LABEL = "Number of dice";
    private static final String SUCCESS_THRESHOLD_LABEL = "Success Threshold";
    private static final int DICE_NUM_MIN = 0;
    private static final int DICE_NUM_MAX = 25;
    private static final int HGAP = 2;
    private static final int VGAP = 10;
    private static final String TITLE_TEXT = "Dice Roller";
    private static final int TITLE_SIZE = 20;

    public Window() {
        initialise();
    }

    private void initialise() {
        jFrame = new JFrame("Dice Roller");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
        jFrame.setMinimumSize(new Dimension(400,100));

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        titleContainer = createTitle();
        inputContainer = createInteractionObjects();
        rollContainer = createRollButton();
        responseContainer = createResponseArea();

        diceNumber.setValue("1");
        successThreshold.setValue("1");

        container.add(titleContainer);
        container.add(inputContainer);
        container.add(rollContainer);
        container.add(responseContainer);

        jFrame.add(container);
        jFrame.pack();

        validateInput();
    }

    private JPanel createInteractionObjects() {
        JPanel inputContainer = new JPanel(new GridLayout(0, 2, HGAP, VGAP));

        diceTypeComboBox = new DiceTypeComboBox(DICE_TYPE_LABEL);
        diceTypeComboBox.getComboBox().addActionListener(this);
        diceNumber = createNumberField(DICE_NUM_LABEL, 0, 25);
        successThreshold = createNumberField(SUCCESS_THRESHOLD_LABEL, 0, 1);

        inputContainer.add(diceTypeComboBox.getLabelPanel());
        inputContainer.add(diceTypeComboBox.getBoxPanel());
        inputContainer.add(diceNumber.getLabelPanel());
        inputContainer.add(diceNumber.getFieldPanel());
        inputContainer.add(successThreshold.getLabelPanel());
        inputContainer.add(successThreshold.getFieldPanel());

        return inputContainer;
    }

    private JPanel createResponseArea() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BorderLayout());
        textArea = new JTextArea(10, 10);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setText("Waiting for first roll. . .");
        panel.add(textArea, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel createTitle() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title = new JLabel(TITLE_TEXT);
        title.setFont(new Font("default", Font.BOLD, TITLE_SIZE));
        panel.add(title);

        return panel;
    }

    private JPanel createRollButton() {
        JPanel rollPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        rollButton = new JButton("Roll");
        rollButton.addActionListener(this);
        rollPanel.add(rollButton);

        return rollPanel;
    }

    private InputField createNumberField(String name, int min, int max) {
        InputField field = new InputField(name);
        field.setMin(min);
        field.setMax(max);
        field.getField().getDocument().addDocumentListener(this);

        return field;
    }

    // Implement ActionListener.
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == diceTypeComboBox.getComboBox()) {
            validateInput();
        } else if(e.getSource() == rollButton) {
            response = new Request(diceTypeComboBox.getSelected().getFaces(),
                                   diceNumber.getValue(),
                                   successThreshold.getValue());
            textArea.setText(createResponseText(response));
        }
    }

    private String createResponseText(Request response) {
        StringBuilder sb = new StringBuilder();
        List<Integer> rolls = response.getRolls();
        List<Integer> successfulRolls = response.getSuccessfulRolls();

        sb.append("Rolls: [" + rolls.get(0).toString());
        for(int i = 1; i < rolls.size(); i++) {
            sb.append(", ");
            if(i % 10 == 0) {
                sb.append("\n           ");
            }
            sb.append(rolls.get(i).toString());
        }
        sb.append("]\nTotal: " + response.getTotal() + "\n\n");

        sb.append("Successes (" + successThreshold.getValue() + " or above): "
                  + successfulRolls.size() + "\nSuccessful rolls: ");
        if(successfulRolls.size() == 0) {
            sb.append("NONE");
        } else {
            sb.append("[" + successfulRolls.get(0)
                    .toString());
            for(int i = 1; i < successfulRolls.size(); i++) {
                sb.append(", ");
                if(i % 10 == 0) {
                    sb.append("\n                            ");
                }
                sb.append(successfulRolls.get(i).toString());
            }
            sb.append("]");
        }
        sb.append("\nSuccess total: " + response.getSuccessfulTotal());

        return(sb.toString());
    }

    // Implement DocumentListener.
    @Override
    public void changedUpdate(DocumentEvent e) {
        validateInput();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        validateInput();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        validateInput();
    }

    private void validateInput() {
        DiceType diceType = diceTypeComboBox.getSelected();
        successThreshold.setMax(diceType.getFaces());

        boolean valid = diceNumber.isValid();
        valid &= successThreshold.isValid();

        rollButton.setEnabled(valid);
    }
}