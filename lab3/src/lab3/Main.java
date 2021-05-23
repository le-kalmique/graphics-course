package lab3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lab3.drawing.ZipperFly;

public class Main extends Application {
    public static final int SCENE_WIDTH = 900, SCENE_HEIGHT = 550;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        ZipperFly zipper = new ZipperFly(root);
        zipper.draw();

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
