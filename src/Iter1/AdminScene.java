package Iter1;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class AdminScene {

    private UI2 app;
    private Scene scene;

    public AdminScene(UI2 app) {
        this.app = app;
        createScene();
    }

    private void createScene() {

        StackPane layout = new StackPane();
        scene = new Scene(layout, 700, 650);
    }

    public Scene getScene() {
        return scene;
    }
}
