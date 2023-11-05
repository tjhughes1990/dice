package dice.ui.output;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import dice.core.DiceRoller;
import dice.core.types.DiceCollection;
import dice.ui.DiceCollectionConsumer;
import dice.ui.Utils;

/**
 * Output controller.
 */
public class OutputController implements Initializable {

    private static final URL TITLED_PANE_FXML = OutputController.class.getResource("DiceCollectionView.fxml");

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox diceOutputContainer;

    @FXML
    private TextField grandTotalText;

    private DiceCollectionConsumer backBtnCallback;

    private final ObservableList<DiceCollection> diceCollectionList = FXCollections.observableArrayList();

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        diceCollectionList.addListener((final ListChangeListener.Change<? extends DiceCollection> c) -> {
            // Perform roll.
            final DiceRoller roller = new DiceRoller(diceCollectionList);
            roller.performRoll();

            // Update results.
            createTitledPanes();

            final int grandTotal = diceCollectionList.stream().mapToInt(d -> d.getRolls().total()).sum();
            grandTotalText.setText(String.valueOf(grandTotal));
        });
    }

    private void createTitledPanes() {
        final ObservableList<Node> children = diceOutputContainer.getChildren();
        final int existingPanesCount = children.size();

        final int newPanesCount = diceCollectionList.size();
        final List<TitledPane> newPanes = new ArrayList<>(newPanesCount);
        for (int i = 0; i < newPanesCount; i++) {
            final Optional<TitledPane> newPane = createAccordionLeaf(diceCollectionList.get(i));
            if (newPane.isPresent()) {
                final boolean isExpanded = i < existingPanesCount && ((TitledPane) children.get(i)).isExpanded();
                final TitledPane titledPane = newPane.get();
                titledPane.setExpanded(isExpanded);
                newPanes.add(titledPane);
            }
        }
        children.setAll(newPanes);
    }

    public void setDiceCollectionList(final List<DiceCollection> diceCollectionList) {
        this.diceCollectionList.setAll(diceCollectionList);
    }

    private Optional<TitledPane> createAccordionLeaf(final DiceCollection diceCollection) {
        final FXMLLoader loader = new FXMLLoader(TITLED_PANE_FXML, Utils.RESOURCES);

        final DiceCollectionController controller = new DiceCollectionController(diceCollection);
        loader.setController(controller);

        try {
            return Optional.ofNullable(loader.load());
        } catch (final IOException e) {
            return Optional.empty();
        }
    }

    /**
     * @param backBtnCallback the back btn callback to set.
     */
    public void setBackBtnCallback(final DiceCollectionConsumer backBtnCallback) {
        this.backBtnCallback = backBtnCallback;
    }

    @FXML
    private void onClickBackBtn() throws IOException {
        if (backBtnCallback != null) {
            backBtnCallback.accept(diceCollectionList);
        }
    }

    @FXML
    private void onClickRerollBtn() {
        final List<DiceCollection> newDiceCollectionList = new ArrayList<>(diceCollectionList);
        setDiceCollectionList(newDiceCollectionList);
    }
}
