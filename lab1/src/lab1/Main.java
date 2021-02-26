package lab1;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import javafx.scene.shape.*;

public class Main extends Application {
  public static final String TITLE = "Lab 1";
  public static final int SCENE_WIDTH = 600, SCENE_HEIGHT = 350;
  public static final int TV_WIDTH = 300, TV_HEIGHT = 200;
  public static final int SCREEN_WIDTH = 200, SCREEN_HEIGHT = 165;
  public static final int BUTTON_RADIUS = 7;

  @Override
  public void start(Stage primaryStage) {
    Group root = new Group();
    Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

    // CREATING SCENE -----------------------
    drawRectangle(
      root,
      new Coordinates( SCENE_WIDTH / 2. - TV_WIDTH / 2., SCENE_HEIGHT / 2. - TV_HEIGHT / 2.),
      TV_WIDTH, TV_HEIGHT, Color.rgb(255, 165, 0)
    );
    drawRectangle(
      root,
      new Coordinates(SCENE_WIDTH / 2. - TV_WIDTH / 2. + 20, SCENE_HEIGHT / 2. - SCREEN_HEIGHT / 2.),
      SCREEN_WIDTH, SCREEN_HEIGHT, Color.rgb(128, 128, 128), 30
    );

    for (int i = 0; i < 3; i++) {
      drawCircle(
        root,
        new Coordinates(SCENE_WIDTH / 2. + TV_WIDTH / 2. - 30, SCENE_HEIGHT / 2. + ((i + 1) * BUTTON_RADIUS) + (i * 25)),
        BUTTON_RADIUS, Color.BLACK
      );
    }

    Coordinates antennasStartingCoords = new Coordinates(SCENE_WIDTH / 2., SCENE_HEIGHT / 2. - TV_HEIGHT / 2.);
    drawLine(
      root,
      antennasStartingCoords,
      new Coordinates(SCENE_WIDTH / 2. - 50, SCENE_HEIGHT / 2. - TV_HEIGHT / 2. - 50), Color.BLACK
    );
    drawLine(
      root,
      antennasStartingCoords,
      new Coordinates(SCENE_WIDTH / 2. + 50, SCENE_HEIGHT / 2. - TV_HEIGHT / 2. - 50), Color.BLACK
    );
    // --------------------------------------

    scene.setFill(Color.rgb(127, 255, 0));
    primaryStage.setTitle(TITLE);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private static class Coordinates {
    private final double _x;
    private final double _y;

    public Coordinates(double x, double y) {
      _x = x;
      _y = y;
    }

    public double x() {
      return _x;
    }
    public double y() {
      return _y;
    }
  }

  private void drawRectangle(Group root, Coordinates coords, int width, int height, Color color, int... arch) {
    Rectangle rectangle = new Rectangle();
    rectangle.setX(coords.x());
    rectangle.setY(coords.y());
    rectangle.setWidth(width);
    rectangle.setHeight(height);
    rectangle.setFill(color);
    if (arch.length > 0) {
      rectangle.setArcWidth(arch[0]);
      rectangle.setArcHeight(arch[0]);
    }
    root.getChildren().add(rectangle);
  }

  private void drawCircle(Group root, Coordinates coords, int radius, Color color) {
    Circle circle = new Circle();
    circle.setCenterX(coords.x());
    circle.setCenterY(coords.y());
    circle.setRadius(radius);
    circle.setFill(color);
    root.getChildren().add(circle);
  }

  private void drawLine(Group root, Coordinates startCoords, Coordinates endCoords, Color color) {
    Line line = new Line(startCoords.x(), startCoords.y(), endCoords.x(), endCoords.y());
    line.setStroke(color);
    root.getChildren().add(line);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
