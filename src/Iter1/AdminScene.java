package Iter1;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AdminScene {

    private UI2 app;
    private Scene scene;


    public AdminScene(UI2 app) {
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

        Label issueCatLab = new Label("Issue;" );
        issueCatLab.setStyle("-fx-font-size: 24px;");

        VBox mainContainer = new VBox(50); // 10px spacing between boxes

        // Define styling for the box borders
        String borderStyle = "-fx-padding: 15; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 5;";

        // ROAD Category
        HBox roadBox = new HBox(20);
        roadBox.setStyle(borderStyle);
        roadBox.setAlignment(Pos.TOP_LEFT); // Align content to the left
        roadBox.setPrefWidth(120);
        Label roadLabel = new Label("ROAD");
        CheckBox roadCheckBox = new CheckBox();
        roadBox.getChildren().addAll(roadLabel, roadCheckBox);

        // VANDALISM Category
        HBox vandalismBox = new HBox(5);
        vandalismBox.setStyle(borderStyle);
        vandalismBox.setAlignment(Pos.TOP_LEFT); // Align content to the left
        vandalismBox.setPrefWidth(120);
        Label vandalismLabel = new Label("VANDALISM");
        CheckBox vandalismCheckBox = new CheckBox();
        vandalismBox.getChildren().addAll(vandalismLabel, vandalismCheckBox);

        // ELECTRICAL Category
        HBox electricalBox = new HBox(5);
        electricalBox.setStyle(borderStyle);
        Label electricalLabel = new Label("ELECTRICAL");
        CheckBox electricalCheckBox = new CheckBox();
        electricalBox.getChildren().addAll(electricalLabel, electricalCheckBox);

        // WATER Category
        HBox waterBox = new HBox(5);
        waterBox.setStyle(borderStyle);
        Label waterLabel = new Label("WATER");
        CheckBox waterCheckBox = new CheckBox();
        waterBox.getChildren().addAll(waterLabel, waterCheckBox);

        // OBSTRUCTION Category
        HBox obstructionBox = new HBox(5);
        obstructionBox.setStyle(borderStyle);
        Label obstructionLabel = new Label("OBSTRUCTION");
        CheckBox obstructionCheckBox = new CheckBox();
        obstructionBox.getChildren().addAll(obstructionLabel, obstructionCheckBox);

        // OTHER Category
        HBox otherBox = new HBox(5);
        otherBox.setStyle(borderStyle);
        Label otherLabel = new Label("OTHER");
        CheckBox otherCheckBox = new CheckBox();
        otherBox.getChildren().addAll(otherLabel, otherCheckBox);

        // Add each category box to the main container
        VBox categoriesContainer = new VBox(20);
        categoriesContainer.getChildren().addAll(
                roadBox, vandalismBox, electricalBox, waterBox, obstructionBox, otherBox
        );
        mainContainer.getChildren().add(categoriesContainer);

        // Set up the scene
        scene = new Scene(mainContainer, 700, 600);
    }

    public Scene getScene() {
        return scene;
    }
}
