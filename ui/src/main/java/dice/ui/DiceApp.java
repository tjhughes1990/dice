package dice.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Dice app.
 */
public class DiceApp extends Application {

    @Override
    public void start(final Stage stage) throws IOException {
        stage.setTitle(Utils.RESOURCES.getString("app_title"));

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
