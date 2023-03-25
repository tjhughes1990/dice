package dice.ui.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import dice.core.types.DiceCollection;
import dice.ui.Utils;

/**
 * Config controller.
 */
public class ListController implements Initializable {

    @FXML
    private ListView<DiceCollection> diceCollectionListView;

    @FXML
    private Button addBtn;

    @FXML
    private Button removeBtn;

    private final List<DiceCollectionListener> listeners = new ArrayList<>();

    private final ObservableList<DiceCollection> diceCollectionList = FXCollections.observableArrayList();

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        if (diceCollectionList.isEmpty()) {
            diceCollectionList.add(new DiceCollection(generateDiceCollectionName()));
        }

        diceCollectionList.addListener((ListChangeListener.Change<? extends DiceCollection> c) -> {
            setRemoveBtnEnabledState();
        });
        setRemoveBtnEnabledState();

        diceCollectionListView.setCellFactory(v -> new ListCell<>() {
            @Override
            protected void updateItem(final DiceCollection item, final boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName().toString());
                }
            }
        });
        diceCollectionListView.setItems(diceCollectionList);

        final DiceCollection diceCollection = diceCollectionList.get(0);
        diceCollectionListView.getSelectionModel().select(diceCollection);
        diceCollectionListView.getFocusModel().focus(0);

        notifyListeners();
    }

    private void setRemoveBtnEnabledState() {
        removeBtn.setDisable(diceCollectionList.size() <= 1);
    }

    private String generateDiceCollectionName() {
        final String prefix = Utils.RESOURCES.getString("config_dice_collection_lbl");
        int i = 1;
        String name = String.format("%s %d", prefix, i);

        final Set<String> existingNames = new HashSet<>(
                diceCollectionList.stream().map(DiceCollection::getName).toList());

        while (existingNames.contains(name)) {
            name = String.format("%s %d", prefix, ++i);
        }

        return name;
    }

    public ObservableList<DiceCollection> getDiceCollectionList() {
        return diceCollectionList;
    }

    public void setDiceCollectionList(final List<DiceCollection> diceCollectionList) {
        this.diceCollectionList.setAll(diceCollectionList);

        if (this.diceCollectionList.isEmpty()) {
            this.diceCollectionList.add(new DiceCollection(generateDiceCollectionName()));
        }
        notifyListeners();
    }

    public void addListener(final DiceCollectionListener listener) {
        listeners.add(listener);
        listener.update(diceCollectionListView.getSelectionModel().getSelectedItem());
    }

    private void notifyListeners() {
        final DiceCollection diceCollection = diceCollectionListView.getSelectionModel().getSelectedItem();
        listeners.forEach(l -> l.update(diceCollection));
    }

    @FXML
    private void onClickAddBtn() {
        diceCollectionList.add(new DiceCollection(generateDiceCollectionName()));
    }

    @FXML
    private void onClickRemoveBtn() {
        if (diceCollectionList.size() > 1) {
            final int selectedInd = diceCollectionListView.getSelectionModel().getSelectedIndex();
            diceCollectionList.remove(selectedInd);
        }
    }

    @FXML
    private void onClickDiceCollection() {
        notifyListeners();
    }
}
