package dice.ui.config;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import dice.core.types.Dice;
import dice.core.types.DiceCollection;

public class TableViewController implements Initializable {

    @FXML
    private TableView<DiceCollection> tableView;

    @FXML
    private TableColumn<TableView<DiceCollection>, String> nameCol;

    @FXML
    private TableColumn<TableView<DiceCollection>, Dice> typeCol;

    @FXML
    private TableColumn<TableView<DiceCollection>, Integer> countCol;

    private final ObservableList<DiceCollection> diceCollectionList = FXCollections.observableArrayList();

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(e -> setNewValue(e, s -> s != null && !s.isBlank(), DiceCollection::setName));

        typeCol.setCellValueFactory(new PropertyValueFactory<>("dice"));
        final ObservableList<Dice> diceTypeList = FXCollections.observableArrayList(Dice.DEFAULT_DICE);
        typeCol.setCellFactory(ComboBoxTableCell.forTableColumn(diceTypeList));
        typeCol.setOnEditCommit(e -> setNewValue(e, v -> v != null, DiceCollection::setDice));

        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        countCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        countCol.setOnEditCommit(e -> setNewValue(e, v -> v > 0, DiceCollection::setCount));

        tableView.setItems(diceCollectionList);
    }

    private <T> void setNewValue(final CellEditEvent<TableView<DiceCollection>, T> event,
                                 final Predicate<T> isValidFunc,
                                 final BiConsumer<DiceCollection, T> setterFunc) {

        final T newValue = event.getNewValue();
        if (isValidFunc.test(newValue)) {
            final int row = event.getTablePosition().getRow();
            final DiceCollection diceCollection = diceCollectionList.get(row);
            setterFunc.accept(diceCollection, newValue);
        }
    }

    public List<DiceCollection> getDiceCollectionList() {
        return diceCollectionList.stream().toList();
    }

    public void setDiceCollectionList(final List<DiceCollection> diceCollectionList) {
        this.diceCollectionList.setAll(diceCollectionList);
    }

    public void addDiceCollection(final DiceCollection diceCollection) {
        diceCollectionList.add(diceCollection);
    }

    public void removeDiceCollection() {
        final TableViewSelectionModel<DiceCollection> selectionModel = tableView.getSelectionModel();
        int toRemoveIndex = selectionModel.getSelectedIndex();
        final DiceCollection toRemove = selectionModel.getSelectedItem();

        diceCollectionList.remove(toRemove);

        final int newIndex = Math.max(0, --toRemoveIndex);
        selectionModel.select(newIndex);
    }

    public void addListener(final Runnable listener) {
        diceCollectionList.addListener((ListChangeListener<? super DiceCollection>) c -> listener.run());
    }
}
