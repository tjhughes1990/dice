package dice.ui.output;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

import dice.core.types.DiceCollection;
import dice.core.types.DiceRolls;

/**
 * Controller for DiceCollectionView.
 */
public class DiceCollectionController implements Initializable {

    @FXML
    private TitledPane pane;

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField rollsText;

    @FXML
    private TextField minText;

    @FXML
    private TextField maxText;

    @FXML
    private TextField totalText;

    private final DiceCollection diceCollection;

    public DiceCollectionController(final DiceCollection diceCollection) {
        this.diceCollection = diceCollection;
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        final String diceCollectionName = diceCollection.getName();
        final String diceCollectionDesc = diceCollection.getDescription();
        final String title = String.format("%s - %s", diceCollectionName, diceCollectionDesc);
        pane.setText(title);

        final DiceRolls rolls = diceCollection.getRolls();
        final List<Integer> values = rolls.values();
        final List<String> valuesStrList = values.stream().map(String::valueOf).toList();
        final String valuesStr = String.join(", ", valuesStrList);
        rollsText.setText(valuesStr);

        final boolean areMultipleValues = values.size() > 1;
        final List<TextField> multiValueFields = List.of(minText, maxText, totalText);
        final List<TextField> visibleFields = new ArrayList<>(List.of(rollsText));
        if (areMultipleValues) {
            minText.setText(String.valueOf(rolls.min()));
            maxText.setText(String.valueOf(rolls.max()));
            totalText.setText(String.valueOf(rolls.total()));

            visibleFields.addAll(multiValueFields);
        } else {
            removeSummaryRows(multiValueFields);
        }

        visibleFields.forEach(t -> t.prefWidthProperty().bind(pane.prefWidthProperty()));
    }

    private void removeSummaryRows(final List<TextField> fieldsToRemove) {
        final Set<Integer> rowIndicesToRemove = new HashSet<>(
                fieldsToRemove.stream().map(GridPane::getRowIndex).toList());
        final List<Node> childrenToRemove = new ArrayList<>();

        final List<Node> children = gridPane.getChildren();
        for (final Node child : children) {
            Integer row = GridPane.getRowIndex(child);
            row = row == null ? 0 : row;
            if (rowIndicesToRemove.contains(row)) {
                childrenToRemove.add(child);
            }
        }

        children.removeAll(childrenToRemove);
    }
}
