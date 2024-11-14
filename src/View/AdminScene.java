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
//        Label overskriftLab = new Label("URBAN ISSUE REPORTING");
//        overskriftLab.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        SubTitle underOverskriftLab = new SubTitle("''Your City, Your Voice, Your Impact''");
//        underOverskriftLab.setStyle("-fx-font-size: 20px;");

        Separator separator = new Separator();
        separator.setPrefWidth(700);

        // Issue category checkboxes
        ArrayList<CategoryCheckBox> checkBoxes = new ArrayList<>();
        for(Pair<Integer, String> category : categories) {
//            checkBoxes.add(new CheckBox(category.getValue()));
            checkBoxes.add(new CategoryCheckBox(category.getKey(), category.getValue()));
        }
        CheckBox showResolvedCheckbox = new CheckBox("Resolved Issues");

        // "Update" button to fetch and display issues
        Button updateButton = new Button("Update");

        // Create TableView and set its data
        TableView<Issue> tableView = new TableView<>();
        updateIssues(tableView, checkBoxes, showResolvedCheckbox);
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

        TextField newCategoryField = new TextField();
        newCategoryField.setPromptText("Enter new category name");

        Button addCategoryButton = new Button("Add New Category");

        addCategoryButton.setOnAction(e -> {
            String newCategoryName = newCategoryField.getText().trim().toLowerCase();
            if(newCategoryName.isEmpty()) {
                System.out.println("Error: Category name cannot be empty.");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setHeaderText(null);
                alert.setContentText("Category name cannot be empty");
                alert.showAndWait();
                return;
            }
            if(!dbAdmin.addNewCategory(newCategoryName)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Failed to add category to database.");
                alert.showAndWait();
                return;
            }

            newCategoryField.clear();
            categories = dbAdmin.fetchCategories(); // Update the category list
            // add functionality so that the user can see the newly created category:
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("The new Category has been successfully made!");
            alert.showAndWait();
        });

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

        HBox newCatLay = new HBox(10,newCategoryField,addCategoryButton);
        newCatLay.setAlignment(Pos.BOTTOM_CENTER);
        newCatLay.setPadding(new Insets(10));

        VBox categoryLayout = new VBox(10);
        categoryLayout.getChildren().addAll(checkBoxes);
        categoryLayout.getChildren().addAll(showResolvedCheckbox);
        categoryLayout.getChildren().add(updateButton);
        categoryLayout.setAlignment(Pos.TOP_LEFT);
        categoryLayout.setPadding(new Insets(10));

        HBox tablecatLay = new HBox(10,categoryLayout,tableView);
        tablecatLay.setAlignment(Pos.TOP_LEFT);
        tablecatLay.setPadding(new Insets(10));

        VBox mainLayout = new VBox(10, overskriftLab, underOverskriftLab, separator, tablecatLay,newCatLay);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setAlignment(Pos.TOP_CENTER);

        // Set up the scene
        scene = new Scene(mainLayout, 1000, 700);

        updateButton.setOnAction(e -> updateIssues(tableView, checkBoxes, showResolvedCheckbox));
    }

    public Scene getScene() {
        return scene;
    }

    // Method to update issues in the tableView based on selected categories
    private void updateIssues(TableView<Issue> tableView, ArrayList<CategoryCheckBox> checkBoxes, CheckBox showResolvedCheckbox) {
        ArrayList<Pair<Integer, String>> selectedCategories = new ArrayList<>();

        for(CategoryCheckBox checkBox : checkBoxes)
            if (checkBox.isSelected())
                for (Pair<Integer, String> category : categories)
                    if (checkBox.getCategoryId() == category.getKey())
                        selectedCategories.add(category);

        if(selectedCategories.isEmpty())
            selectedCategories.addAll(categories);

        ObservableList<Issue> issues = getIssueData(selectedCategories, showResolvedCheckbox.isSelected());
        tableView.setItems(issues);
    }

    private ObservableList<Issue> getIssueData(ArrayList<Pair<Integer, String>> selectedCategories, boolean showResolved){
        ArrayList<Issue> issues = dbAdmin.fetchIssuesByCategories(selectedCategories);

        if (!showResolved) {
            issues.removeIf(issue -> issue.getStatus() == Status.RESOLVED);
        }
        return FXCollections.observableArrayList(
                issues
        );
    }
}