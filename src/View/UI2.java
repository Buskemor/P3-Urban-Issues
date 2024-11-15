package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UI2 extends Application {

    Stage stage;
    Scene sceneWho, sceneIssue, sceneAdmin, sceneConfirmation, sceneAdvancedSettings;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setTitle("Urban Issues");

        // Create instances of each scene class
        WhoScene whoScene = new WhoScene(this);
        IssueScene issueScene = new IssueScene(this);
        AdminScene adminScene = new AdminScene(this);
        ConfirmationScene confirmationScene = new ConfirmationScene(this);
        AdvancedSettingsScene advancedSettingsScene = new AdvancedSettingsScene(this);

        // Initialize scenes
        sceneWho = whoScene.getScene();
        sceneIssue = issueScene.getScene();
        sceneAdmin = adminScene.getScene();
        sceneConfirmation = confirmationScene.getScene();
        sceneAdvancedSettings = advancedSettingsScene.getScene();

        // Set the initial scene
        primaryStage.setScene(sceneWho);
        primaryStage.show();
    }

    // Method to switch scenes from other classes
    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    public Scene getSceneWho() {
        return sceneWho;
    }

    public Scene getSceneIssue() {
        return sceneIssue;
    }

    public Scene getSceneAdmin() {
        return sceneAdmin;
    }

    public Scene getSceneConfirmation() {
    return sceneConfirmation;
}

    public Scene getSceneAdvancedSettings() {
        return sceneAdvancedSettings;
    }


}

