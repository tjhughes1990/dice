module dice.ui {
    exports dice.ui;

    requires dice.core;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    opens dice.ui to javafx.graphics;
    opens dice.ui.config to javafx.fxml;
    opens dice.ui.output to javafx.fxml;
}
