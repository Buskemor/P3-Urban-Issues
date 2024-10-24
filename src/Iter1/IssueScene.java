package Iter1;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static java.awt.SystemColor.control;


public class IssueScene {

    private UI2 app;
    private Scene scene;

    public IssueScene(UI2 app) {
        this.app = app;
        createScene();
    }

    private void createScene() {
        Label header = new Label("Issue Reporting");
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

        /*HBox layoutname = new HBox(10); // 10px spacing between buttons
        layoutname.getChildren().addAll(namelab, nametext);
        layoutname.setAlignment(Pos.CENTER); // Center the buttons horizontally

        HBox layoutcat = new HBox(10); // 10px spacing between buttons
        layoutcat.getChildren().addAll(categorylab, categoryBox);
        layoutcat.setAlignment(Pos.CENTER); // Center the buttons horizontally

        HBox layoutdesc = new HBox(10); // 10px spacing between buttons
        layoutdesc.getChildren().addAll(descriptionlab, descriptiontext);
        layoutdesc.setAlignment(Pos.CENTER); // Center the buttons horizontally

        //IMAGE TEXT

        HBox layoutloc = new HBox(10); // 10px spacing between buttons
        layoutloc.getChildren().addAll(locationlab, locationtext);
        layoutloc.setAlignment(Pos.CENTER); // Center the buttons horizontally

        HBox layoutfeed = new HBox(10); // 10px spacing between buttons
        layoutfeed.getChildren().addAll(feedbacklab, feedbackBox);
        layoutfeed.setAlignment(Pos.CENTER); // Center the buttons horizontally

        HBox layoutemail = new HBox(10); // 10px spacing between buttons
        layoutemail.getChildren().addAll(emaillab, emailtext);
        layoutemail.setAlignment(Pos.CENTER); // Center the buttons horizontally*/

        Button submitknap= new Button("Submit");



        // Layout for Header and Separator
        /*VBox headerLayout = new VBox(10); // 10px spacing
        headerLayout.getChildren().addAll(header, separator, underoverskrift, layoutcat, layoutdesc, imagelab,layoutloc,layoutfeed,layoutemail, submitknap);
        headerLayout.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        headerLayout.setAlignment(Pos.TOP_CENTER); // Centered at the top*/

        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);

        grid.add(namelab,0,0,1,1);
        grid.add(nametext,1,0,1,1);

        grid.add(categorylab,0,1,1,1);
        grid.add(categoryBox,1,1,1,1);

        grid.add(descriptionlab,0,2,1,1);
        grid.add(descriptiontext,1,2,1,1);

        grid.add(imagelab,0,3,1,1);

        grid.add(locationlab,0,4,1,1);
        grid.add(locationtext,1,4,1,1);

        grid.add(feedbacklab,0,5,1,1);
        grid.add(feedbackBox,1,5,1,1);

        grid.add(emaillab,0,6,1,1);
        grid.add(emailtext,1,6,1,1);

        grid.setHgap(20);
        grid.setVgap(10);

        grid.setPadding(new Insets(10,10,10,10));

        ColumnConstraints column1= new ColumnConstraints();
        ColumnConstraints column2= new ColumnConstraints();

        grid.getColumnConstraints().add(column1);
        grid.getColumnConstraints().add(column2);

        column1.setPrefWidth(200);


        GridPane.setHalignment(namelab, HPos.RIGHT);
        GridPane.setHalignment(categorylab, HPos.RIGHT);
        GridPane.setHalignment(descriptionlab, HPos.RIGHT);
        GridPane.setHalignment(imagelab, HPos.RIGHT);
        GridPane.setHalignment(locationlab, HPos.RIGHT);
        GridPane.setHalignment(feedbacklab, HPos.RIGHT);
        GridPane.setHalignment(emaillab, HPos.RIGHT);





        // Create the Scene
        scene = new Scene(grid, 700, 650);
    }

    public Scene getScene() {
        return scene;
    }
}
