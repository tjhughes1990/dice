package dice.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Dice app.
 */
public class DiceApp extends Application {

    @Override
    public void start(final Stage stage) throws IOException {
        stage.setTitle(Utils.RESOURCES.getString("app_title"));
        stage.getIcons().add(new Image(DiceApp.class.getResourceAsStream("icons/dice-icon-256.png")));

        stage.setMinWidth(800.0);
        stage.setMinHeight(600.0);

        SceneMgr.INSTANCE.setStage(stage);
        SceneMgr.INSTANCE.switchToConfigScene();

        stage.show();
    }

    /**
     * Main.
     *
     * @param args
     */
    public static void main(final String[] args) {
        launch();
    }
}
