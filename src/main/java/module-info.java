module com.tetris137 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.tetris137 to javafx.fxml;
    exports com.tetris137;
}
