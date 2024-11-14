package View;


import View.JavaFXExtensions.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ConfirmationScene {
    private UI2 app;
    private Scene scene;


    public ConfirmationScene(UI2 app) {
        this.app = app;
        createScene();
    }

    private void createScene() {
        Title overskriftLab = new Title("URBAN ISSUE REPORTING");
//        overskriftLab.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        SubTitle underOverskriftLab = new SubTitle("''Your City, Your Voice, Your Impact''");
//        underOverskriftLab.setStyle("-fx-font-size: 20px;");

        // Create Separator
        Separator separator = new Separator();
        separator.setPrefWidth(700); // Adjust the width to match your scene width

        Image image = new Image(getClass().getResource("confirmation.png").toExternalForm()); // Replace with your image file path
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(150); // Adjust the width as needed
        imageView.setPreserveRatio(true); // Maintain the aspect ratio of the image


        Label thanklab = new Label("Thank you!");
        thanklab.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label submitlab = new Label("Your issue has been submitted.");
        submitlab.setStyle("-fx-font-size: 18px;");

        Label referlab = new Label("Please refer to your email for our response");
        referlab.setStyle("-fx-font-size: 16px;");

        VBox centerLayout = new VBox(10); // 10px spacing
        centerLayout.getChildren().addAll(imageView,thanklab,submitlab,referlab);
        centerLayout.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        centerLayout.setAlignment(Pos.CENTER); // Centered at the top

        VBox headerLayout = new VBox(10); // 10px spacing
        headerLayout.getChildren().addAll(overskriftLab, underOverskriftLab,separator);
        headerLayout.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        headerLayout.setAlignment(Pos.TOP_CENTER); // Centered at the top


        BorderPane root = new BorderPane();
        root.setTop(headerLayout);

        root.setCenter(centerLayout);

        scene = new Scene(root, 700, 600);

    }

    public Scene getScene() {
        return scene;
    }

}