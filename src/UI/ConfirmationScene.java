package UI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

public class ConfirmationScene {
    private UI2 app;
    private Scene scene;


    public ConfirmationScene(UI2 app) {
        this.app = app;
        createScene();
    }

    private void createScene() {
        Label overskriftLab = new Label("URBAN ISSUE REPORTING");
        overskriftLab.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label underOverskriftLab = new Label("''Your City, Your Voice, Your Impact''");
        underOverskriftLab.setStyle("-fx-font-size: 20px;");

        // Create Separator
        Separator separator = new Separator();
        separator.setPrefWidth(700); // Adjust the width to match your scene width

        VBox headerLayout = new VBox(10); // 10px spacing
        headerLayout.getChildren().addAll(overskriftLab, underOverskriftLab,separator);
        headerLayout.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        headerLayout.setAlignment(Pos.TOP_CENTER); // Centered at the top



        scene = new Scene(headerLayout, 700, 600);

    }

    public Scene getScene() {
        return scene;
    }

}