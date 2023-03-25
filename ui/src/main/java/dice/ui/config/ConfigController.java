package dice.ui.config;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import dice.core.types.DiceCollection;
import dice.ui.DiceCollectionConsumer;

/**
 * Config controller.
 */
public class ConfigController implements Initializable {

    @FXML
    private ListController listController;

    @FXML
    private ValuesController valuesController;

    @FXML
    private Button rollBtn;

    private DiceCollectionConsumer rollBtnCallback;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        listController.addListener(valuesController);
    }

    public void setDiceCollectionList(final List<DiceCollection> diceCollectionList) {
        listController.setDiceCollectionList(diceCollectionList);
    }

    public void setRollBtnCallback(final DiceCollectionConsumer rollBtnCallback) {
        this.rollBtnCallback = rollBtnCallback;
    }

    @FXML
    private void onClickRollBtn() throws IOException {
        if (rollBtnCallback != null) {
            final List<DiceCollection> diceCollectionList = listController.getDiceCollectionList().stream().toList();
            rollBtnCallback.accept(diceCollectionList);
        }
    }
}
