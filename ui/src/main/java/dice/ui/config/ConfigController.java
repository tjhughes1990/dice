package dice.ui.config;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import dice.core.types.DiceCollection;
import dice.ui.DiceCollectionConsumer;
import dice.ui.Utils;
import dice.ui.io.SerialisationDialogHandler;

/**
 * Config controller.
 */
public class ConfigController implements Initializable {

    @FXML
    private TableViewController tableViewController;

    @FXML
    private Button removeBtn;

    @FXML
    private Button rollBtn;

    private DiceCollectionConsumer rollBtnCallback;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        if (tableViewController.getDiceCollectionList().isEmpty()) {
            onClickAddBtn();
        }
        setRemoveBtnEnabledState();

        tableViewController.addListener(this::setRemoveBtnEnabledState);
    }

    private DiceCollection generateDiceCollection() {
        final String prefix = Utils.RESOURCES.getString("config_dice_collection_lbl");
        int i = 1;
        String name = String.format("%s %d", prefix, i);

        final List<DiceCollection> diceCollectionList = tableViewController.getDiceCollectionList();
        final Set<String> existingNames = new HashSet<>(
                diceCollectionList.stream().map(DiceCollection::getName).toList());

        while (existingNames.contains(name)) {
            name = String.format("%s %d", prefix, ++i);
        }

        return new DiceCollection(name);
    }

    private void setRemoveBtnEnabledState() {
        removeBtn.setDisable(tableViewController.getDiceCollectionList().size() <= 1);
    }

    public void setDiceCollectionList(final List<DiceCollection> diceCollectionList) {
        tableViewController.setDiceCollectionList(diceCollectionList);
    }

    public void setRollBtnCallback(final DiceCollectionConsumer rollBtnCallback) {
        this.rollBtnCallback = rollBtnCallback;
    }

    @FXML
    private void onClickAddBtn() {
        tableViewController.addDiceCollection(generateDiceCollection());
    }

    @FXML
    private void onClickRemoveBtn() {
        tableViewController.removeDiceCollection();
    }

    @FXML
    private void onClickSaveBtn() {
        SerialisationDialogHandler.saveDiceCollectionList(tableViewController.getDiceCollectionList());
    }

    @FXML
    private void onClickLoadBtn() {
        SerialisationDialogHandler.loadDiceCollectionList().ifPresent(this::setDiceCollectionList);
    }

    @FXML
    private void onClickRollBtn() throws IOException {
        if (rollBtnCallback != null) {
            rollBtnCallback.accept(tableViewController.getDiceCollectionList());
        }
    }
}
