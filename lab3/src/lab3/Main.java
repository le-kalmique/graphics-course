package lab3;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.stage.Stage;
import javafx.util.Duration;

import lab3.drawing.ZipperFly;

public class Main extends Application {
    public static final int SCENE_WIDTH = 900, SCENE_HEIGHT = 550;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        ZipperFly zipper = new ZipperFly(root);
        zipper.draw();

        Path movementPath = new Path(
                new MoveTo(-50, 100),
                new QuadCurveTo(SCENE_WIDTH / 2., SCENE_HEIGHT - 100, SCENE_WIDTH + 10, 100)
        );
        PathTransition movement = new PathTransition();
        movement.setDuration(Duration.seconds(5));
        movement.setPath(movementPath);
        movement.setNode(root);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(5), root);
        scale.setToX(0.3);
        scale.setToY(0.3);

        RotateTransition rotate = new RotateTransition(Duration.millis(2500), root);
        rotate.setByAngle(360);
        rotate.setCycleCount(2);


        ParallelTransition parallel = new ParallelTransition();
        parallel.getChildren().addAll(movement, scale, rotate);

        parallel.setCycleCount(ParallelTransition.INDEFINITE);
        parallel.play();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
