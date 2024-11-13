package View;

import Model.*;
import Controller.DbManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Date;

public class AdminScene {

    private UI2 app;
    private Scene scene;
    private DbManager dbManager;
    private ArrayList<Pair<Integer, String>> categories;


    public AdminScene(UI2 app) {
        this.app = app;
        this.dbManager = new DbManager();
        this.categories = dbManager.fetchCategories();
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
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        for(Pair<Integer, String> category : categories) {
            checkBoxes.add(new CheckBox(category.getValue()));
        }

        // "Update" button to fetch and display issues
        Button updateButton = new Button("Update");

        // Create TableView and set its data
        TableView<Issue> tableView = new TableView<>();
        updateIssues(tableView, checkBoxes);

        // Create columns
        TableColumn<Issue, Integer> issueIdCol = new TableColumn<>("ID");
        issueIdCol.setCellValueFactory(new PropertyValueFactory<>("issueId"));
        issueIdCol.setSortable(true);
        issueIdCol.setPrefWidth(30);

        TableColumn<Issue, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setSortable(true);

        TableColumn<Issue, String> roadCol = new TableColumn<>("Road");
        roadCol.setCellValueFactory(new PropertyValueFactory<>("road"));
        roadCol.setSortable(true);

        TableColumn<Issue, Integer> houseNumberCol = new TableColumn<>("No.");
        houseNumberCol.setCellValueFactory(new PropertyValueFactory<>("houseNumber"));
        houseNumberCol.setSortable(false);

        TableColumn<Issue, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionCol.setSortable(false);
        descriptionCol.setPrefWidth(200);

        TableColumn<Issue, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryDisplayString"));
        categoryCol.setSortable(true);

        TableColumn<Issue, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setSortable(true);

        TableColumn<Issue, String> citizenEmailCol = new TableColumn<>("Email");
        citizenEmailCol.setCellValueFactory(cellData -> {
            Citizen citizen = cellData.getValue().getReportedBy();
            return new SimpleStringProperty(citizen != null ? citizen.getEmail() : "N/A");
        });
        citizenEmailCol.setSortable(true);
        citizenEmailCol.setPrefWidth(150);

        // Add columns to TableView
        tableView.getColumns().addAll(issueIdCol, dateCol, roadCol, houseNumberCol, descriptionCol, categoryCol, statusCol, citizenEmailCol);


        // Layout for the categories and ListView
//        VBox categoryLayout = new VBox(10, roadCheckBox, vandalismCheckBox, electricalCheckBox, waterCheckBox, obstructionCheckBox, otherCheckBox, updateButton);
        VBox categoryLayout = new VBox(10);
        categoryLayout.getChildren().addAll(checkBoxes);
        categoryLayout.getChildren().add(updateButton);
        categoryLayout.setAlignment(Pos.TOP_LEFT);
        categoryLayout.setPadding(new Insets(10));

        HBox tablecatLay = new HBox(10,categoryLayout,tableView);
        tablecatLay.setAlignment(Pos.TOP_LEFT);
        tablecatLay.setPadding(new Insets(10));

        VBox mainLayout = new VBox(10, overskriftLab, underOverskriftLab, separator, tablecatLay);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setAlignment(Pos.TOP_CENTER);

        // Set up the scene
        scene = new Scene(mainLayout, 1000, 700);

        updateButton.setOnAction(e -> updateIssues(tableView, checkBoxes));
    }

    public Scene getScene() {
        return scene;
    }

    // Method to update issues in the tableView based on selected categories
    private void updateIssues(TableView<Issue> tableView, ArrayList<CheckBox> checkBoxes) {
        ArrayList<Pair<Integer, String>> selectedCategories = new ArrayList<>();

        for(CheckBox checkBox : checkBoxes)
            if(checkBox.isSelected())
                for(Pair<Integer, String> category : categories)
                    if(checkBox.getText().equals(category.getValue()))
                        selectedCategories.add(category);

        if(selectedCategories.isEmpty())
            selectedCategories.addAll(categories);

        ObservableList<Issue> issues = getIssueData(selectedCategories);
        tableView.setItems(issues);
    }

    private ObservableList<Issue> getIssueData(ArrayList<Pair<Integer, String>> selectedCategories) {
        ArrayList<Issue> issues = dbManager.fetchIssuesByCategories(selectedCategories);
        return FXCollections.observableArrayList(
                issues
        );
    }
}