package Iter1;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import static java.awt.SystemColor.control;


public class IssueScene {

    private UI2 app;
    private Scene scene;


    public IssueScene(UI2 app) {
        this.app = app;
        createScene();
    }

    private void createScene() {
        Label header = new Label("Urban Issue Reporting");
        header.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;"); // Style the header

        // Create Separator
        Separator separator = new Separator();
        separator.setPrefWidth(700); // Adjust the width to match your scene width

        // Create Body Content
        Label overskrift = new Label("Report an issue");
        overskrift.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;");

        Label underoverskrift = new Label("Please describe the issue below:");
        underoverskrift.setStyle("-fx-font-size: 16px;");

        Label namelab = new Label("Name*:");
        namelab.setStyle("-fx-font-size: 16px;");
        TextField nametext = new TextField();

        Label categorylab= new Label("Category*:");
        categorylab.setStyle("-fx-font-size: 16px;");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.setPrefWidth(300);

        // Adding items to the ComboBox
        categoryBox.getItems().add("Road");
        categoryBox.getItems().add("Vandalism");
        categoryBox.getItems().add("Electrical");
        categoryBox.getItems().add("Water");
        categoryBox.getItems().add("Obstruction");
        categoryBox.getItems().add("Other");

        Label descriptionlab = new Label("Description*:");
        descriptionlab.setStyle("-fx-font-size: 16px;");
        TextField descriptiontext = new TextField();

        Label imagelab = new Label("Image:");
        imagelab.setStyle("-fx-font-size: 16px;");


        Label locationlab = new Label("Location of issue*:");
        locationlab.setStyle("-fx-font-size: 16px;");
        TextField locationtext = new TextField();

        Label feedbacklab= new Label("Receive feedback?:");
        feedbacklab.setStyle("-fx-font-size: 16px;");
        CheckBox feedbackBox = new CheckBox();

        Label emaillab = new Label("E-mail:");
        emaillab.setStyle("-fx-font-size: 16px;");
        TextField emailtext = new TextField();


        Button submitknap= new Button("Submit");
        submitknap.setOnAction(e -> {
            //Retrieving data
            String name = nametext.getText();
            String category = categoryBox.getValue();
            String description = descriptiontext.getText();
            String location = locationtext.getText();
            Boolean feedback = feedbackBox.isSelected();
            String email = emailtext.getText();


            // Check if required fields are filled
            if (name.isEmpty() || category == null || description.isEmpty() || location.isEmpty()) {
                // Alert user if any required field is missing
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setHeaderText(null);
                alert.setContentText("Please fill out the missing information.");
                alert.showAndWait();

            } else {
                // Print information to console
                String finalname = name;
                String finalcategory = category;
                String finaldescription = description;
                String finallocation = location;
                Boolean finalfeedback = feedback;
                String finalemail = email;

                System.out.println(finalname);
                System.out.println(finalcategory);
                System.out.println(finaldescription);
                System.out.println(finallocation);
                System.out.println(finalfeedback);
                System.out.println(finalemail);

                // Exit application
                javafx.application.Platform.exit();
            }
        });

        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);

        grid.add(namelab,1,5,1,1);
        grid.add(nametext,2,5,12,1);

        grid.add(categorylab,1,6,1,1);
        grid.add(categoryBox,2,6,12,1);

        grid.add(descriptionlab,1,7,1,1);
        grid.add(descriptiontext,2,7,12,1);

        grid.add(imagelab,1,8,1,1);

        grid.add(locationlab,1,9,1,1);
        grid.add(locationtext,2,9,12,1);

        grid.add(feedbacklab,1,10,1,1);
        grid.add(feedbackBox,2,10,1,1);

        grid.add(emaillab,1,11,1,1);
        grid.add(emailtext,2,11,12,1);

        grid.add(submitknap,7,15,1,1);

        grid.setHgap(20);
        grid.setVgap(10);

        grid.setPadding(new Insets(10,10,10,10));

        ColumnConstraints column1= new ColumnConstraints();
        ColumnConstraints column2= new ColumnConstraints();

        grid.getColumnConstraints().add(column1);
        grid.getColumnConstraints().add(column2);

        column1.setPrefWidth(20);


        GridPane.setHalignment(namelab, HPos.RIGHT);
        GridPane.setHalignment(categorylab, HPos.RIGHT);
        GridPane.setHalignment(descriptionlab, HPos.RIGHT);
        GridPane.setHalignment(imagelab, HPos.RIGHT);
        GridPane.setHalignment(locationlab, HPos.RIGHT);
        GridPane.setHalignment(feedbacklab, HPos.RIGHT);
        GridPane.setHalignment(emaillab, HPos.RIGHT);


        VBox contentBox = new VBox(10,grid);
        contentBox.setAlignment(Pos.TOP_CENTER);

        VBox headerLayout = new VBox(10); // 10px spacing
        headerLayout.getChildren().addAll(header, separator,overskrift, underoverskrift);
        headerLayout.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        headerLayout.setAlignment(Pos.TOP_CENTER); // Centered at the top

        BorderPane root = new BorderPane();
        root.setTop(headerLayout);
        BorderPane.setAlignment(header,Pos.CENTER);
        root.setCenter(contentBox);


        // Create the Scene
        scene = new Scene(root, 700, 600);
    }

    public Scene getScene() {
        return scene;
    }
}
