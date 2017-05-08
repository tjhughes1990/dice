package ui.src.main.java.tim;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ui.src.main.java.tim.CustomFont;
import ui.src.main.java.tim.config.Config;
import ui.src.main.java.tim.input.DiceType;
import ui.src.main.java.tim.input.DiceTypeComboBox;
import ui.src.main.java.tim.input.NumberField;
import ui.src.main.java.tim.input.TickBox;
import ui.src.main.java.tim.output.OutputText;
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
    private NumberField diceNumber;
    private NumberField successThreshold;
    private TickBox maxBox;
    private TickBox oneBox;
    private JButton rollButton;
    private OutputText outputText;

    private Config config;
    private Request response;
    private CustomFont customFont;

    private static final String DICE_TYPE_LABEL = "Dice Type";
    private static final String DICE_NUM_LABEL = "Number of dice";
    private static final String SUCCESS_THRESHOLD_LABEL = "Success Threshold";
    private static final String MAX_COUNTS_DOUBLE =
        "Max roll counts as two successes";
    private static final String ONE_COUNTS_AS_BOTCH =
        "Botch (roll 1 and 0 successes)";

    private static final String DEFAULT_DICE_ID = "DT_D10";
    private static final int DICE_NUM_MAX = 25;
    private static final int HGAP = 2;
    private static final int VGAP = 10;
    private static final String TITLE_TEXT = "Dice Roller";
    private static final int TITLE_SIZE = 36;
    private static final int NORMAL_SIZE = 20;

    public Window() {
        customFont = new CustomFont();
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
        responseContainer = createOutputField("Waiting for first roll. . .");

        setDefaultValues();

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
        diceTypeComboBox.setFont(customFont.getFont(Font.BOLD, NORMAL_SIZE),
            customFont.getFont(NORMAL_SIZE));
        diceTypeComboBox.setSelected(DEFAULT_DICE_ID);
        diceTypeComboBox.addActionListener(this);
        diceNumber = createNumberField(DICE_NUM_LABEL, 0, DICE_NUM_MAX);
        diceNumber.setFont(customFont.getFont(Font.BOLD, NORMAL_SIZE),
            customFont.getFont(NORMAL_SIZE));
        successThreshold = createNumberField(SUCCESS_THRESHOLD_LABEL, 0, 1);
        successThreshold.setFont(customFont.getFont(Font.BOLD, NORMAL_SIZE),
            customFont.getFont(NORMAL_SIZE));
        maxBox = new TickBox(MAX_COUNTS_DOUBLE);
        maxBox.setFont(customFont.getFont(Font.BOLD, NORMAL_SIZE),
            customFont.getFont(NORMAL_SIZE));
        oneBox = new TickBox(ONE_COUNTS_AS_BOTCH);
        oneBox.setFont(customFont.getFont(Font.BOLD, NORMAL_SIZE),
            customFont.getFont(NORMAL_SIZE));

        inputContainer.add(diceTypeComboBox.getLabelPanel());
        inputContainer.add(diceTypeComboBox.getBoxPanel());
        inputContainer.add(diceNumber.getLabelPanel());
        inputContainer.add(diceNumber.getFieldPanel());
        inputContainer.add(successThreshold.getLabelPanel());
        inputContainer.add(successThreshold.getFieldPanel());
        inputContainer.add(maxBox.getLabelPanel());
        inputContainer.add(maxBox.getCheckBoxPanel());
        inputContainer.add(oneBox.getLabelPanel());
        inputContainer.add(oneBox.getCheckBoxPanel());

        return inputContainer;
    }

    private NumberField createNumberField(String name, int min, int max) {
        NumberField field = new NumberField(name);
        field.setMin(min);
        field.setMax(max);
        field.addDocumentListener(this);

        return field;
    }

    private JPanel createOutputField(String defaultText) {
        JPanel outputPanel = new JPanel();
        outputText = new OutputText(defaultText);
        outputText.setFont(customFont.getFont(NORMAL_SIZE));

        outputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outputPanel.setLayout(new BorderLayout());
        outputPanel.add(outputText.getOutputArea(), BorderLayout.CENTER);

        return outputPanel;
    }

    private JPanel createRollButton() {
        JPanel rollPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        rollButton = new JButton("Roll");
        rollButton.setFont(customFont.getFont(Font.BOLD, NORMAL_SIZE));
        rollButton.addActionListener(this);
        rollPanel.add(rollButton);

        return rollPanel;
    }

    private JPanel createTitle() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title = new JLabel(TITLE_TEXT);
        title.setFont(customFont.getFont(Font.BOLD, (float)TITLE_SIZE));
        panel.add(title);

        return panel;
    }

    private void setDefaultValues() {
        Config config = new Config();
        diceNumber.setValue(config.getDiceNumberDefault().toString());
        successThreshold.setValue(
            config.getSuccessThresholdDefault().toString());
        maxBox.setSelected(config.getMaxRollDefault());
        oneBox.setSelected(config.getBotchDefault());
    }

    // Implement ActionListener.
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == diceTypeComboBox.getComboBox()) {
            validateInput();
        } else if(e.getSource() == rollButton) {
            response = new Request(diceTypeComboBox.getSelected().getFaces(),
                                   diceNumber.getValue(),
                                   successThreshold.getValue(),
                                   maxBox.isSelected(), oneBox.isSelected());
            try {
                outputText.populate(response);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                    "ERROR: Could not populate output.");
            }
        }
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