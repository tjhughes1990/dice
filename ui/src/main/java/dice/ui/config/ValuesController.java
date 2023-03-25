package dice.ui.config;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import dice.core.types.Dice;
import dice.core.types.DiceCollection;
import dice.ui.Utils;

/**
 * Config controller.
 */
public class ValuesController implements Initializable, DiceCollectionListener {

    @FXML
    private ComboBox<Dice> diceCombo;

    @FXML
    private TextField diceCountTxt;

    private final ObjectProperty<DiceCollection> diceCollectionProp = new SimpleObjectProperty<>();

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        diceCombo.setItems(FXCollections.observableArrayList(Dice.DEFAULT_DICE));

        diceCountTxt.setTextFormatter(Utils.getIntFormatter());

        diceCollectionProp.addListener((p, o, n) -> {
            if (n == null) {
                n = new DiceCollection("");
            }

            diceCombo.getSelectionModel().select(n.getDice());
            diceCountTxt.setText(String.valueOf(n.getCount()));
        });
    }

    @Override
    public void update(final DiceCollection diceCollection) {
        diceCollectionProp.setValue(diceCollection);
    }

    @FXML
    private void onSelectDice() {
        final DiceCollection diceCollection = diceCollectionProp.getValue();
        if (diceCollection != null) {
            diceCollection.setDice(diceCombo.getValue());
        }
    }

    @FXML
    private void onChangeCount() {
        final DiceCollection diceCollection = diceCollectionProp.getValue();
        if (diceCollection == null) {
            return;
        }

        try {
            final int count = Integer.parseInt(diceCountTxt.getText());
            diceCollection.setCount(count);
        } catch (final NumberFormatException e) {
            // Nothing to do.
        }
    }
}
