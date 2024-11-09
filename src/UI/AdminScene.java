package UI;


import Iter1.Category;
import database.DbManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class AdminScene {

    private UI2 app;
    private Scene scene;
    private DbManager dbManager; // DbManager instance to interact with the database
    private ListView<String> issueListView; // ListView to display issues

    private CheckBox roadCheckBox;
    private CheckBox vandalismCheckBox;
    private CheckBox electricalCheckBox;
    private CheckBox waterCheckBox;
    private CheckBox obstructionCheckBox;
    private CheckBox otherCheckBox;

    public AdminScene(UI2 app) {
        this.app = app;
        this.dbManager = new DbManager(); // Initialize DbManager
        createScene();
    }

    private void createScene() {
        // Existing labels and layout components
        Label overskriftLab = new Label("URBAN ISSUE REPORTING");
        overskriftLab.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label underOverskriftLab = new Label("''Your City, Your Voice, Your Impact''");
        underOverskriftLab.setStyle("-fx-font-size: 20px;");

        Separator separator = new Separator();
        separator.setPrefWidth(700);

        // Issue category checkboxes
        roadCheckBox = new CheckBox("ROAD");
        vandalismCheckBox = new CheckBox("VANDALISM");
        electricalCheckBox = new CheckBox("ELECTRICAL");
        waterCheckBox = new CheckBox("WATER");
        obstructionCheckBox = new CheckBox("OBSTRUCTION");
        otherCheckBox = new CheckBox("OTHER");

        // "Update" button to fetch and display issues
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateIssues());

        // Issue ListView to display retrieved issues
        issueListView = new ListView<>();

        // Layout for the categories and ListView
        VBox categoryLayout = new VBox(10, roadCheckBox, vandalismCheckBox, electricalCheckBox, waterCheckBox, obstructionCheckBox, otherCheckBox, updateButton);
        categoryLayout.setAlignment(Pos.TOP_LEFT);
        categoryLayout.setPadding(new Insets(10));

        VBox mainLayout = new VBox(10, overskriftLab, underOverskriftLab, separator, categoryLayout, issueListView);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setAlignment(Pos.TOP_CENTER);

        // Set up the scene
        scene = new Scene(mainLayout, 900, 700);
    }

    public Scene getScene() {
        return scene;
    }

    // Method to update issues in the ListView based on selected categories
    private void updateIssues() {
        // Get selected categories
        List<Category> selectedCategories = new ArrayList<>();
        if (roadCheckBox.isSelected()) selectedCategories.add(Category.ROAD);
        if (vandalismCheckBox.isSelected()) selectedCategories.add(Category.VANDALISM);
        if (electricalCheckBox.isSelected()) selectedCategories.add(Category.ELECTRICAL);
        if (waterCheckBox.isSelected()) selectedCategories.add(Category.WATER);
        if (obstructionCheckBox.isSelected()) selectedCategories.add(Category.OBSTRUCTION);
        if (otherCheckBox.isSelected()) selectedCategories.add(Category.OTHER);

        // Fetch issues from the database
        List<String> issues = dbManager.fetchIssuesByCategories(selectedCategories);

        // Display issues in the ListView
        ObservableList<String> observableIssues = FXCollections.observableArrayList(issues);
        issueListView.setItems(observableIssues);
    }
}