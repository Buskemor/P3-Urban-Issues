package View;

import Model.*;
import Controller.DbAdmin;

import View.JavaFXExtensions.SubTitle;
import View.JavaFXExtensions.Title;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.Date;

public class AdminScene {

    private UI2 app;
    private Scene scene;
    private DbAdmin dbAdmin;
    private ArrayList<Pair<Integer, String>> categories;


    public AdminScene(UI2 app) {
        this.app = app;
        this.dbAdmin = new DbAdmin("root", "KENDATABASE123", "localhost", "3306", "issuesdb");
        this.categories = dbAdmin.fetchCategories();
        createScene();
    }


    private void createScene() {
        // Existing labels and layout components
        Title overskriftLab = new Title("URBAN ISSUE REPORTING");

        SubTitle underOverskriftLab = new SubTitle("''Your City, Your Voice, Your Impact''");

        Separator separator = new Separator();
        separator.setPrefWidth(700);

        Label statusLabel = new Label("Status filter");
        statusLabel.setStyle("-fx-font-weight: bold;");


        Label categoryLabel = new Label("Category filter");
        categoryLabel.setStyle("-fx-font-weight: bold;");

        // Issue category checkboxes
        ArrayList<CategoryCheckBox> checkBoxes = new ArrayList<>();
        for(Pair<Integer, String> category : categories) {
            checkBoxes.add(new CategoryCheckBox(category.getKey(), category.getValue()));
        }
        CheckBox showResolvedCheckbox = new CheckBox("Resolved Issues");
        CheckBox showCancelledCheckBox = new CheckBox("Cancelled Issues");

        // "Update" button to fetch and display issues
        Button updateButton = new Button("Update");

        // Create TableView and set its data
        TableView<Issue> tableView = new TableView<>();
        updateIssues(tableView, checkBoxes, showResolvedCheckbox, showCancelledCheckBox);
        tableView.setPrefWidth(820);

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

        Button buttonAdvancedSettingsScene = new Button("Go to Advanced Settings");
        buttonAdvancedSettingsScene.setOnAction(e -> app.setScene(app.getSceneAdvancedSettings()));


        TableColumn<Issue, Status> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setSortable(true);

        // Setting a ComboBox as a cell editor for the Status column
        statusCol.setCellFactory(column -> new TableCell<>() {
            private final ComboBox<Status> statusDropdown = new ComboBox<>(FXCollections.observableArrayList(Status.values()));

            {
                statusDropdown.setOnAction(event -> {
                    Status newStatus = statusDropdown.getValue();
                    Issue currentIssue = getTableView().getItems().get(getIndex());
                    dbAdmin.updateIssueStatus(currentIssue, newStatus); // Update in DB
                    currentIssue.setStatus(newStatus); // Update in the model
                    getTableView().refresh(); // Refresh table to show updated status maybe doesnt do anything
                });
            }

            @Override
            protected void updateItem(Status status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                } else {
                    statusDropdown.setValue(status); // Set current status
                    setGraphic(statusDropdown); // Display ComboBox in cell
                }
            }
        });


        TableColumn<Issue, String> citizenEmailCol = new TableColumn<>("Email");
        citizenEmailCol.setCellValueFactory(cellData -> {
            Citizen citizen = cellData.getValue().getReportedBy();
            return new SimpleStringProperty(citizen != null ? citizen.getEmail() : "N/A");
        });
        citizenEmailCol.setSortable(true);
        citizenEmailCol.setPrefWidth(150);

        // Add columns to TableView
        tableView.getColumns().addAll(issueIdCol, dateCol, roadCol, houseNumberCol, descriptionCol, categoryCol, statusCol, citizenEmailCol);

        addContextMenuToTable(tableView);


        // Create a spacer to add space between the checkbox and the categories
        Label spacer = new Label();
        spacer.setPrefHeight(20); // Adjust height as needed

        VBox categoryLayout = new VBox(10);
        categoryLayout.getChildren().addAll(statusLabel);
        categoryLayout.getChildren().addAll(showResolvedCheckbox, showCancelledCheckBox);
        categoryLayout.getChildren().addAll(spacer);
        categoryLayout.getChildren().addAll(categoryLabel);
        categoryLayout.getChildren().addAll(checkBoxes);
        categoryLayout.getChildren().add(updateButton);
        categoryLayout.setAlignment(Pos.TOP_LEFT);
        categoryLayout.setPadding(new Insets(10));

        HBox tablecatLay = new HBox(10,categoryLayout,tableView);
        tablecatLay.setAlignment(Pos.TOP_LEFT);
        tablecatLay.setPadding(new Insets(10));

        HBox advancedButton = new HBox(10,buttonAdvancedSettingsScene);
        advancedButton.setAlignment(Pos.TOP_RIGHT);
        advancedButton.setPadding(new Insets(10,40,10,10));


        VBox mainLayout = new VBox(10, overskriftLab, underOverskriftLab, separator, advancedButton,tablecatLay);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setAlignment(Pos.TOP_CENTER);



        // Set up the scene
        scene = new Scene(mainLayout, 1030, 630);

        updateButton.setOnAction(e -> updateIssues(tableView, checkBoxes, showResolvedCheckbox, showCancelledCheckBox));
    }


    private void addContextMenuToTable(TableView<Issue> tableView) {

        tableView.setRowFactory(tv -> {
            TableRow<Issue> row = new TableRow<>();

            // Create a ContextMenu for each row
            ContextMenu contextMenu = new ContextMenu();

            // Create "Copy to Clipboard" menu item
            MenuItem copyItem = new MenuItem("Copy to Clipboard");
            copyItem.setOnAction(event -> {
                Issue selectedIssue = row.getItem(); // Get the issue for this row
                if (selectedIssue != null) {
                    copyToClipboard(selectedIssue); // Copy data to clipboard
                }
            });

            // Add the menu item to the ContextMenu
            contextMenu.getItems().add(copyItem);

            // Show the context menu when the user right-clicks on a row
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                } else {
                    contextMenu.hide();
                }
            });

            return row;
        });
    }

    // Method to copy issue details to clipboard
    private void copyToClipboard(Issue issue) {
        String issueDetails = "ID: " + issue.getIssueId() +
                "\nDate: " + issue.getDate() +
                "\nLocation: " + issue.getRoad() +" "+ issue.getHouseNumber()+
                "\nDescription: " + issue.getDescription() +
                "\nCategory: " + issue.getCategoryDisplayString();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(issueDetails);
        clipboard.setContent(content);



//        // Optionally, show a confirmation alert
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Copied to Clipboard");
//        alert.setContentText("Issue details have been copied to your clipboard.");
//        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }

    // Method to update issues in the tableView based on selected categories
    private void updateIssues(TableView<Issue> tableView, ArrayList<CategoryCheckBox> checkBoxes, CheckBox showResolvedCheckbox, CheckBox showCancelledCheckBox) {
        ArrayList<Pair<Integer, String>> selectedCategories = new ArrayList<>();

        for(CategoryCheckBox checkBox : checkBoxes)
            if (checkBox.isSelected())
                for (Pair<Integer, String> category : categories)
                    if (checkBox.getCategoryId() == category.getKey())
                        selectedCategories.add(category);

        if(selectedCategories.isEmpty())
            selectedCategories.addAll(categories);

        ObservableList<Issue> issues = getIssueData(selectedCategories, showResolvedCheckbox.isSelected(), showCancelledCheckBox.isSelected());
        tableView.setItems(issues);
    }

    private ObservableList<Issue> getIssueData(ArrayList<Pair<Integer, String>> selectedCategories, boolean showResolved, boolean showCancelled){
//        TODO: MAKE THIS ARRAYLIST AN ATTRIBUTE
        ArrayList<Issue> issues = dbAdmin.fetchIssuesByCategories(selectedCategories);
        System.out.println(issues);

        if(!showResolved)
            issues.removeIf(issue -> issue.getStatus() == Status.RESOLVED);
        if(!showCancelled)
            issues.removeIf(issue -> issue.getStatus() == Status.CANCELLED);

        if(showResolved && showCancelled) {
            issues.removeIf(issue -> (issue.getStatus() != Status.RESOLVED && issue.getStatus() != Status.CANCELLED));
            System.out.println("both are true");
            return FXCollections.observableArrayList(
                    issues
            );
        }

        if(showResolved)
            issues.removeIf(issue -> issue.getStatus() != Status.RESOLVED);
        if(showCancelled)
            issues.removeIf(issue -> issue.getStatus() != Status.CANCELLED);

        return FXCollections.observableArrayList(
                issues
        );
    }
}