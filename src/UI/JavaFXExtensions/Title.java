package UI.JavaFXExtensions;

import javafx.scene.control.Label;

public class Title extends Label {
    public Title(String text) {
        super(text);
        super.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
    }
}
