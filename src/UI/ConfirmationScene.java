package UI;


import UI.JavaFXExtensions.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ConfirmationScene {
    private UIManager app;
    private Scene scene;


    public ConfirmationScene(UIManager app) {
        this.app = app;
        createScene();
    }

    private void createScene() {
        Title overskriftLab = new Title("URBAN ISSUE REPORTING");
//        overskriftLab.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        SubTitle underOverskriftLab = new SubTitle("''Your City, Your Voice, Your Impact''");
//        underOverskriftLab.setStyle("-fx-font-size: 20px;");

        Separator separator = new Separator();
        separator.setPrefWidth(700);

        Image image = new Image(getClass().getResource("confirmation.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);


        Label thanklab = new Label("Thank you!");
        thanklab.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label submitlab = new Label("Your issue has been submitted.");
        submitlab.setStyle("-fx-font-size: 18px;");

        Label referlab = new Label("Please refer to your email for our response");
        referlab.setStyle("-fx-font-size: 16px;");

        VBox centerLayout = new VBox(10);
        centerLayout.getChildren().addAll(imageView,thanklab,submitlab,referlab);
        centerLayout.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        centerLayout.setAlignment(Pos.CENTER);

        VBox headerLayout = new VBox(10);
        headerLayout.getChildren().addAll(overskriftLab, underOverskriftLab,separator);
        headerLayout.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        headerLayout.setAlignment(Pos.TOP_CENTER);


        BorderPane root = new BorderPane();
        root.setTop(headerLayout);

        root.setCenter(centerLayout);

        scene = new Scene(root, 700, 600);

    }

    public Scene getScene() {
        return scene;
    }

}