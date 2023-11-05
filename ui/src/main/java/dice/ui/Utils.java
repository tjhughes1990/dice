package dice.ui;

import java.util.ResourceBundle;

import javafx.scene.control.TextFormatter;

/**
 * Utility methods and constants.
 */
public class Utils {

    public static final ResourceBundle RESOURCES = ResourceBundle.getBundle("dice/ui/resources");

    private Utils() {
    }

    public static TextFormatter<Integer> getIntFormatter() {
        return new TextFormatter<>(s -> s.getControlNewText().matches("\\d*") ? s : null);
    }
}
