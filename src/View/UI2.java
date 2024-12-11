package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UI2 extends Application {

    private Stage stage;
    private Scene issueScene, adminScene, confirmationScene, advancedSettingsScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setTitle("Urban Issues");

        Scene whoScene = new WhoScene(this).getScene();
        issueScene = new IssueScene(this).getScene();
        adminScene = new AdminScene(this).getScene();
        confirmationScene = new ConfirmationScene(this).getScene();
        advancedSettingsScene = new AdvancedSettingsScene(this).getScene();

        primaryStage.setScene(whoScene);
        primaryStage.show();
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    public Scene getIssueScene() {
        return issueScene;
    }

    public Scene getAdminScene() {
        return adminScene;
    }

    public Scene getConfirmationScene() {
        return confirmationScene;
    }

    public Scene getAdvancedSettingsScene() {
        return advancedSettingsScene;
    }
}