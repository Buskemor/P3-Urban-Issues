package View;

import Controller.DbAdmin;
import View.JavaFXExtensions.SubTitle;
import View.JavaFXExtensions.Title;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;

public class AdvancedSettingsScene {
    private UI2 app;
    private Scene scene;
    private DbAdmin dbAdmin;
    private ArrayList<Pair<Integer, String>> categories;


    public AdvancedSettingsScene(UI2 app) {
        this.app = app;
        this.dbAdmin = new DbAdmin("root", "KENDATABASE123", "localhost", "3306", "issuesdb");
        this.categories = dbAdmin.fetchCategories();
        createScene();
    }

    private void createScene() {
        Title overskriftLab = new Title("URBAN ISSUE REPORTING");

        SubTitle underOverskriftLab = new SubTitle("''Your City, Your Voice, Your Impact''");


        // Create Separator
        Separator separator = new Separator();
        separator.setPrefWidth(700); // Adjust the width to match your scene width

        TextField newCategoryField = new TextField();
        newCategoryField.setPromptText("Enter new category name");
        newCategoryField.setPrefWidth(160);

        Button addCategoryButton = getButton(newCategoryField);


        Button buttonAdmin = new Button("Go back");
        buttonAdmin.setOnAction(e -> app.setScene(new AdminScene(app).getScene()));


//        TextField deleteCategoryField = new TextField();
//        deleteCategoryField.setPromptText("Enter category name to delete");
        Button deleteCategoryButton = new Button("Delete Category");
        deleteCategoryButton.setPrefWidth(120);

        Label categorylab= new Label("Select category to delete");
        categorylab.setStyle("-fx-font-size: 16px;");
        ComboBox<String> categoryBox = new ComboBox<>();
        for(Pair<Integer, String> category : categories) {
            categoryBox.getItems().add(category.getValue());
        }
        categoryBox.setPrefWidth(160);

        deleteCategoryButton.setOnAction(e -> {
            String categoryToDelete = categoryBox.getValue();
            if (categoryToDelete.isEmpty()) {
                showAlert("Missing Information", "Please enter a category name to delete.", Alert.AlertType.WARNING);
                return;
            }
            confirmDeletion(categoryToDelete);
        });


        HBox AddCatLayout = new HBox(10);
        AddCatLayout.getChildren().addAll(newCategoryField,addCategoryButton);
        AddCatLayout.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        AddCatLayout.setAlignment(Pos.TOP_CENTER); // Centered at the top

        HBox DeleteCatLayout = new HBox(10);
//        DeleteCatLayout.getChildren().addAll(deleteCategoryField,deleteCategoryButton);
        DeleteCatLayout.getChildren().addAll(categoryBox,deleteCategoryButton);
        DeleteCatLayout.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        DeleteCatLayout.setAlignment(Pos.TOP_CENTER); // Centered at the top


        VBox headerLayout = new VBox(10); // 10px spacing
        headerLayout.getChildren().addAll(overskriftLab, underOverskriftLab,separator,AddCatLayout,DeleteCatLayout,buttonAdmin);
        headerLayout.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        headerLayout.setAlignment(Pos.TOP_CENTER); // Centered at the top


        BorderPane root = new BorderPane();
        root.setTop(headerLayout);



        scene = new Scene(root, 1200, 800);

    }

    private void confirmDeletion(String categoryToDelete) {
        Alert initialConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        initialConfirmation.setTitle("Delete Category Confirmation");
        initialConfirmation.setHeaderText("Are you sure you want to delete this category?");
        initialConfirmation.setContentText("This category may be associated with existing issues. Proceed with deletion?");

        initialConfirmation.showAndWait().ifPresent(response -> {
            if (response != ButtonType.OK)
                return;
            boolean hasAssociatedIssues = dbAdmin.checkIfCategoryHasIssues(categoryToDelete);
            if (hasAssociatedIssues) {
                confirmIssueDeletion(categoryToDelete);
                return;
            }
            boolean canDeleteCategory = dbAdmin.deleteCategory(categoryToDelete);
            if (canDeleteCategory) {
                showAlert("Success", "The category was successfully deleted.", Alert.AlertType.INFORMATION);
                return;
            }
            showAlert("Error", "Failed to delete the category. Please try again.", Alert.AlertType.ERROR);
        });
    }

    private void confirmIssueDeletion(String categoryToDelete) {
        Alert secondaryConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        secondaryConfirmation.setTitle("Confirm Deletion of Issues");
        secondaryConfirmation.setHeaderText("This category is associated with existing issues.");
        secondaryConfirmation.setContentText("If you proceed, these issues will be reassigned to 'DeletedCategory'. Are you sure you want to continue?");

        secondaryConfirmation.showAndWait().ifPresent(secondResponse -> {
            if (secondResponse == ButtonType.OK) {
                boolean canDeleteCategory = dbAdmin.deleteCategory(categoryToDelete);
                if (canDeleteCategory) {
                    showAlert("Success", "The category and associated issues were successfully updated.", Alert.AlertType.INFORMATION);
                    return;
                }
                showAlert("Error", "Failed to delete the category. Please try again.", Alert.AlertType.ERROR);
            }
        });
    }

    private Button getButton(TextField newCategoryField) {
        Button addCategoryButton = new Button("Add New Category");
        addCategoryButton.setPrefWidth(120);
        addCategoryButton.setOnAction(e -> {
            String newCategoryName = newCategoryField.getText().trim().toLowerCase();
            if(newCategoryName.isEmpty()) {
                System.out.println("Error: Category name cannot be empty.");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setHeaderText(null); //default is WARNING
                alert.setContentText("Category name cannot be empty");
                alert.showAndWait();
                return;
            }

            if(!dbAdmin.addNewCategory(newCategoryName)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Success");
                alert.setHeaderText(null); //default is WARNING
                alert.setContentText("Failed to add category to database.");
                alert.showAndWait();
                return;
            }

            newCategoryField.clear();
            categories = dbAdmin.fetchCategories(); // Update the category list
            // add functionality so that the user can see the newly created category:
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Success");
            alert.setHeaderText(null); //default is WARNING
            alert.setContentText("The new Category has been successfully made!");
            alert.showAndWait();
        });
        return addCategoryButton;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public Scene getScene() {
        return scene;
    }
}