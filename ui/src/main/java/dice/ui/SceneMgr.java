package dice.ui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import dice.core.types.DiceCollection;
import dice.ui.config.ConfigController;
import dice.ui.output.OutputController;

/**
 * Scene manager class.
 */
public class SceneMgr {

    public static final SceneMgr INSTANCE = new SceneMgr();

    private static final URL CONFIG_VIEW_FXML = SceneMgr.class.getResource("config/ConfigView.fxml");
    private static final URL OUTPUT_VIEW_FXML = SceneMgr.class.getResource("output/OutputView.fxml");

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(final Stage stage) {
        this.stage = stage;
    }

    public void switchToConfigScene() throws IOException {
        final Optional<ConfigController> configController = switchScene(CONFIG_VIEW_FXML);
        configController.ifPresent(c -> c.setRollBtnCallback(this::switchToOutputScene));
    }

    public void switchToConfigScene(final List<DiceCollection> diceCollectionList) throws IOException {
        final Optional<ConfigController> configController = switchScene(CONFIG_VIEW_FXML);
        configController.ifPresent(c -> {
            c.setDiceCollectionList(diceCollectionList);
            c.setRollBtnCallback(this::switchToOutputScene);
        });
    }

    private <T> Optional<T> switchScene(final URL sceneUrl) throws IOException {
        if (stage == null) {
            return Optional.empty();
        }

        final FXMLLoader loader = new FXMLLoader(sceneUrl, Utils.RESOURCES);
        final Pane root = loader.load();

        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(root);
        } else {
            scene.setRoot(root);
        }

        stage.setScene(scene);

        return Optional.ofNullable(loader.getController());
    }

    public void switchToOutputScene(final List<DiceCollection> diceCollectionList) throws IOException {
        // Open output view.
        final Optional<OutputController> outputController = switchScene(OUTPUT_VIEW_FXML);
        outputController.ifPresent(c -> {
            c.setDiceCollectionList(diceCollectionList);
            c.setBackBtnCallback(this::switchToConfigScene);
        });
    }
}
