package UI;

import UI.JavaFXExtensions.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class WhoScene {

    private UIManager app;
    private Scene scene;

    public WhoScene(UIManager app) {
        this.app = app;
        createScene();
    }

    private void createScene() {
        Title overskrift = new Title("URBAN ISSUE REPORTING");
//        overskrift.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        SubTitle underOverskrift = new SubTitle("''Your City, Your Voice, Your Impact''");
//        underOverskrift.setStyle("-fx-font-size: 20px;");

        Label whoAreYou = new Label("Who are you?");
        whoAreYou.setStyle("-fx-font-size: 18px;");

        Button buttonCitizen = new Button("Citizen");
        buttonCitizen.setOnAction(e -> app.setScene(app.getIssueScene()));

        Button buttonAdmin = new Button("Administrator");
        buttonAdmin.setOnAction(e -> app.setScene(app.getAdminScene()));

        HBox layout1 = new HBox(10);
        layout1.getChildren().addAll(buttonCitizen, buttonAdmin);
        layout1.setAlignment(Pos.CENTER);

        VBox topLayout = new VBox(10);
        topLayout.getChildren().addAll(overskrift, underOverskrift);
        topLayout.setAlignment(Pos.TOP_CENTER);
        topLayout.setPadding(new javafx.geometry.Insets(50, 0, 0, 0));

        VBox centerLayout = new VBox(20);
        centerLayout.getChildren().addAll(whoAreYou, layout1);
        centerLayout.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(topLayout, centerLayout);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setSpacing(120);

        scene = new Scene(mainLayout, 1030, 630);
    }

    public Scene getScene() {
        return scene;
    }
}
