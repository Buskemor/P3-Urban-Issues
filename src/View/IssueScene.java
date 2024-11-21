package View;
import Controller.DbAdmin;
import Model.Citizen;
import Controller.DbCitizen;

import View.JavaFXExtensions.Title;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Pair;

import java.util.ArrayList;


public class IssueScene {

    private UI2 app;
    private Scene scene;
    private ArrayList<Pair<Integer, String>> categories = new DbAdmin(
            "root",
            "KENDATABASE123",
            "localhost",
            "3306",
            "issuesdb"
    ).fetchCategories();


    public IssueScene(UI2 app) {
        this.app = app;
        createScene();
    }

    private void createScene() {
        Title header = new Title("Urban Issue Reporting");

        // Create Separator
        Separator separator = new Separator();
        separator.setPrefWidth(700); // Adjust the width to match your scene width

        // Create Body Content
        Label overskrift = new Label("Report an issue");
        overskrift.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;");

        Label underoverskrift = new Label("Please describe the issue below:");
        underoverskrift.setStyle("-fx-font-size: 16px;");


        Label categorylab= new Label("Category*:");
        categorylab.setStyle("-fx-font-size: 16px;");

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.setPrefWidth(400);

        // Adding items to the ComboBox
        for(Pair<Integer, String> category : categories) {
            if (!category.getValue().equalsIgnoreCase("deletedcategory")) {
                categoryBox.getItems().add(category.getValue());
            }
        }

        Label descriptionlab = new Label("Description*:");
        descriptionlab.setStyle("-fx-font-size: 16px;");
        TextField descriptiontext = new TextField();

//        Label imagelab = new Label("Image:");
//        imagelab.setStyle("-fx-font-size: 16px;");


        Label locationlab = new Label("Road Name*:");
        locationlab.setStyle("-fx-font-size: 16px;");
        TextField locationtext = new TextField();

        Label numberlab = new Label("Nr*:");
        numberlab.setStyle("-fx-font-size: 16px;");
        TextField numbertext = new TextField();


        Label feedbacklab= new Label("Receive feedback and receive follow-up questions if necessary?:");
        feedbacklab.setStyle("-fx-font-size: 16px;");
        feedbacklab.setWrapText(true); // Enables text wrapping
        feedbacklab.setMaxWidth(150); // Adjust the width to fit your layout
        CheckBox feedbackBox = new CheckBox();

        Label emaillab = new Label("E-mail:");
        emaillab.setStyle("-fx-font-size: 16px;");
        TextField emailtext = new TextField();

        feedbackBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            emaillab.setVisible(newValue);
            emaillab.setManaged(newValue);
            emailtext.setVisible(newValue);
            emailtext.setManaged(newValue);
        });

// Initially hide the email label and text field
        emaillab.setVisible(false);
        emaillab.setManaged(false);
        emailtext.setVisible(false);
        emailtext.setManaged(false);


        Button submitknap= new Button("Submit");
        submitknap.setOnAction(e -> {
            //Retrieving data
            String category = categoryBox.getValue();
            String description = descriptiontext.getText();
            String location = locationtext.getText();
            Boolean feedback = feedbackBox.isSelected();
            String email = emailtext.getText();
            String houseNumber = numbertext.getText();



            // Check if required fields are filled
            if (category == null || description.isEmpty() || location.isEmpty() || !houseNumber.matches("\\d+")) {
                // Alert user if any required field is missing
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setHeaderText(null);
                alert.setContentText("Please fill out the missing information. Nr needs to be a number, no letters");
                alert.showAndWait();

            } else {
                // Print information to console
                int finalhousenumber = Integer.parseInt(houseNumber);

//                String tempcategory = category.toUpperCase(); //going to use this code in DbManager
                Pair<Integer, String> finalCategory = null;

                for(Pair<Integer, String> categoryPair : categories) {
                    if(categoryPair.getValue().equals(category.substring(0,1).toUpperCase() + category.substring(1)))
                        finalCategory = categoryPair;
                }

                DbCitizen issueInserter = null;

                if (feedbackBox.isSelected())
                    issueInserter = new DbCitizen(location, finalhousenumber, description, finalCategory, "root", "KENDATABASE123", "localhost", "3306", "issuesdb", new Citizen(email));
                else
                    issueInserter = new DbCitizen(location, finalhousenumber, description, finalCategory, "root", "KENDATABASE123", "localhost", "3306", "issuesdb");

                issueInserter.addIssueToDatabase();



                app.setScene(app.getSceneConfirmation());

                System.out.println(finalCategory);
                System.out.println(description);
                System.out.println(location);
                System.out.println(feedback);
                System.out.println(email);
                System.out.println(finalhousenumber);
            }
        });

        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);


        grid.add(categorylab,1,6,1,1);
        grid.add(categoryBox,2,6,7,1);

        grid.add(descriptionlab,1,7,1,1);
        grid.add(descriptiontext,2,7,7,1);

//        grid.add(imagelab,1,8,1,1);

        grid.add(locationlab,1,8,1,1);
        grid.add(locationtext,2,8,5,1);

        grid.add(numberlab,7,8,1,1);
        grid.add(numbertext,8,8,1,1);

        grid.add(feedbacklab,1,11,1,1);
        grid.add(feedbackBox,2,11,1,1);

        grid.add(emaillab,1,12,1,1);
        grid.add(emailtext,2,12,7,1);

        grid.add(submitknap,7,15,1,1);

        grid.setHgap(10);
        grid.setVgap(10);

        grid.setPadding(new Insets(10,10,10,10));

        ColumnConstraints column1= new ColumnConstraints();
        ColumnConstraints column2= new ColumnConstraints();

        grid.getColumnConstraints().add(column1);
        grid.getColumnConstraints().add(column2);

        column1.setPrefWidth(20);


        GridPane.setHalignment(categorylab, HPos.RIGHT);
        GridPane.setHalignment(numberlab, HPos.RIGHT);
        GridPane.setHalignment(descriptionlab, HPos.RIGHT);
//        GridPane.setHalignment(imagelab, HPos.RIGHT);
        GridPane.setHalignment(locationlab, HPos.RIGHT);
        GridPane.setHalignment(feedbacklab, HPos.RIGHT);
        GridPane.setHalignment(emaillab, HPos.RIGHT);


        VBox contentBox = new VBox(10,grid);
        contentBox.setAlignment(Pos.TOP_CENTER);

        VBox headerLayout = new VBox(10); // 10px spacing
        headerLayout.getChildren().addAll(header, separator,overskrift, underoverskrift);
        headerLayout.setPadding(new Insets(10, 0, 0, 0));
        headerLayout.setAlignment(Pos.TOP_CENTER); // Centered at the top


        BorderPane root = new BorderPane();
        root.setTop(headerLayout);
        root.setCenter(contentBox);


        // Create the Scene
        scene = new Scene(root, 700, 600);
    }

    public Scene getScene() {
        return scene;
    }
}
