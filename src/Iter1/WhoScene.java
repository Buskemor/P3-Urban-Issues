package Iter1;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class WhoScene {

    private UI2 app;
    private Scene scene;

    public WhoScene(UI2 app) {
        this.app = app;
        createScene();
    }

    private void createScene() {
        Label overskrift = new Label("URBAN ISSUE REPORTING");
        overskrift.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        Label underOverskrift = new Label("''Your City, Your Voice, Your Impact''");
        underOverskrift.setStyle("-fx-font-size: 20px;");

        Label whoAreYou = new Label("Who are you?");
        whoAreYou.setStyle("-fx-font-size: 18px;");

        Button buttonCitizen = new Button("Citizen");
        buttonCitizen.setOnAction(e -> app.setScene(app.getSceneIssue()));

        Button buttonAdmin = new Button("Administrator");
        buttonAdmin.setOnAction(e -> app.setScene(app.getSceneAdmin()));

        // HBox for the buttons (horizontal layout)
        HBox layout1 = new HBox(10); // 10px spacing between buttons
        layout1.getChildren().addAll(buttonCitizen, buttonAdmin);
        layout1.setAlignment(Pos.CENTER); // Center the buttons horizontally

        // VBox for top content (overskrift and underOverskrift)
        VBox topLayout = new VBox(10); // Spacing between overskrift and underOverskrift
        topLayout.getChildren().addAll(overskrift, underOverskrift);
        topLayout.setAlignment(Pos.TOP_CENTER);// Align them at the top and center horizontally
        topLayout.setPadding(new javafx.geometry.Insets(50, 0, 0, 0));

        // VBox for center content (whoAreYou and buttons)
        VBox centerLayout = new VBox(20); // Spacing between whoAreYou and buttons
        centerLayout.getChildren().addAll(whoAreYou, layout1);
        centerLayout.setAlignment(Pos.CENTER); // Center this VBox vertically and horizontally

        // Main VBox to hold top and center layouts
        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(topLayout, centerLayout);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setSpacing(120); // Spacing between the top and center parts

        // Create the scene using mainLayout as the root
        scene = new Scene(mainLayout, 700, 600);
    }

    public Scene getScene() {
        return scene;
    }
}
