package Iter1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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

        Label issueCatLab = new Label("Issue: ");
        issueCatLab.setStyle("-fx-font-size: 24px;");

        // Define styling for the box borders
        String borderStyle = "-fx-padding: 10; -fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 0;";

        // ROAD Category
        HBox roadBox = new HBox(50);
        roadBox.setStyle(borderStyle);
        roadBox.setAlignment(Pos.TOP_LEFT); // Align content to the left
        roadBox.setPrefWidth(120);
        Label roadLabel = new Label("ROAD");
        CheckBox roadCheckBox = new CheckBox();
        roadBox.getChildren().addAll(roadLabel, roadCheckBox);

        // VANDALISM Category
        HBox vandalismBox = new HBox(17);
        vandalismBox.setStyle(borderStyle);
        vandalismBox.setAlignment(Pos.TOP_LEFT); // Align content to the left
        vandalismBox.setPrefWidth(125);
        Label vandalismLabel = new Label("VANDALISM");
        CheckBox vandalismCheckBox = new CheckBox();
        vandalismBox.getChildren().addAll(vandalismLabel, vandalismCheckBox);



        // ELECTRICAL Category
        HBox electricalBox = new HBox(21);
        electricalBox.setStyle(borderStyle);
        Label electricalLabel = new Label("ELECTRICAL");
        CheckBox electricalCheckBox = new CheckBox();
        electricalBox.getChildren().addAll(electricalLabel, electricalCheckBox);

        // WATER Category
        HBox waterBox = new HBox(45);
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
        HBox otherBox = new HBox(47);
        otherBox.setStyle(borderStyle);
        Label otherLabel = new Label("OTHER");
        CheckBox otherCheckBox = new CheckBox();
        otherBox.getChildren().addAll(otherLabel, otherCheckBox);

        Label categorylab = new Label("Category:");
        categorylab.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        categorylab.setAlignment(Pos.TOP_LEFT); // Align content to the left
        categorylab.setPrefWidth(100);

        Label datelab = new Label("Date:");
        datelab.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        datelab.setAlignment(Pos.TOP_LEFT); // Align content to the left
        datelab.setPrefWidth(70);

        Label descriptionlab = new Label("Description:");
        descriptionlab.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        descriptionlab.setAlignment(Pos.TOP_LEFT); // Align content to the left
        descriptionlab.setPrefWidth(150);

        Label imagelab = new Label("Image:");
        imagelab.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        imagelab.setAlignment(Pos.TOP_LEFT); // Align content to the left
        imagelab.setPrefWidth(90);

        Label emaillab = new Label("E-mail:");
        emaillab.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        emaillab.setAlignment(Pos.TOP_LEFT); // Align content to the left
        emaillab.setPrefWidth(100);

        Label statuslab = new Label("Status:");
        statuslab.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        statuslab.setAlignment(Pos.TOP_LEFT); // Align content to the left
        statuslab.setPrefWidth(100);

        Label issueCatlab = new Label("Issue Category:");
        issueCatlab.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");




        // Add each category box to the main container

        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);

        grid.add(issueCatlab,1,1,1,1);
        grid.add(categorylab,2,1,1,1);
        grid.add(datelab,3,1,1,1);
        grid.add(descriptionlab,4,1,1,1);
        grid.add(imagelab,5,1,1,1);
        grid.add(emaillab,6,1,1,1);
        grid.add(statuslab,7,1,1,1);

        grid.add(roadBox,1,2,1,1);
        grid.add(vandalismBox,1,3,1,1);
        grid.add(electricalBox,1,4,1,1);
        grid.add(waterBox,1,5,1,1);
        grid.add(obstructionBox,1,6,1,1);
        grid.add(otherBox,1,7,1,1);


        grid.setHgap(10);
        grid.setVgap(-1);

        grid.setPadding(new Insets(10,10,10,10));

        VBox headerLayout = new VBox(10); // 10px spacing
        headerLayout.getChildren().addAll(overskriftLab, underOverskriftLab,separator,issueCatLab,grid);
        headerLayout.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        headerLayout.setAlignment(Pos.TOP_CENTER); // Centered at the top

        // Set up the scene
        scene = new Scene(headerLayout, 900, 700);
    }

    public Scene getScene() {
        return scene;
    }
}
