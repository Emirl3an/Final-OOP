package sample;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;



public class Main extends Application {

    public Button button;
    int BLOCK_SIZE = 30;
    int WIDTH = 15 * BLOCK_SIZE;
    int HEIGHT = 10 * BLOCK_SIZE;
    double timeForFrame = 0.2;

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    Direction direction = Direction.RIGHT;

    Stage primaryStage;
    BorderPane rootLayout;

    boolean isMoved = false;
    boolean isApplicationRunning = false;

    Timeline animation = new Timeline();


    ObservableList<Node> snakeList;

    public Parent drawMethod() {

        Pane root = new Pane();
        root.setPrefSize(WIDTH, HEIGHT);

        Group snakeBody = new Group();
        snakeList = snakeBody.getChildren();

        Rectangle food = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        food.setFill(Color.RED);
        food.setTranslateX(randomWithRange(BLOCK_SIZE, WIDTH - BLOCK_SIZE));
        food.setTranslateY(randomWithRange(BLOCK_SIZE, HEIGHT - BLOCK_SIZE));

        KeyFrame frame = new KeyFrame(Duration.seconds(timeForFrame), event -> {
            if (!isApplicationRunning) {
                return;
            }

            boolean toRemove = snakeList.size() > 1;

            Node tail = toRemove ? snakeList.remove(snakeList.size() - 1) : snakeList.get(0);

            switch (direction) {
                case UP:
                    tail.setTranslateX(snakeList.get(0).getTranslateX());
                    tail.setTranslateY(snakeList.get(0).getTranslateY() - BLOCK_SIZE);
                    break;
                case DOWN:
                    tail.setTranslateX(snakeList.get(0).getTranslateX());
                    tail.setTranslateY(snakeList.get(0).getTranslateY() + BLOCK_SIZE);
                    break;
                case LEFT:
                    tail.setTranslateX(snakeList.get(0).getTranslateX() - BLOCK_SIZE);
                    tail.setTranslateY(snakeList.get(0).getTranslateY());
                    break;
                case RIGHT:
                    tail.setTranslateX(snakeList.get(0).getTranslateX() + BLOCK_SIZE);
                    tail.setTranslateY(snakeList.get(0).getTranslateY());
                    break;
            }

            isMoved = true;

            if (toRemove) {
                snakeList.add(0, tail);
            }


            for (Node node : snakeList) {
                if (node != tail && tail.getTranslateX() == node.getTranslateX() &&
                        tail.getTranslateY() == node.getTranslateY()) {
                    restartGame();
//                    animation.stop();
                    break;
                }
            }

            if (tail.getTranslateX() < 0 || tail.getTranslateX() >= WIDTH ||
                    tail.getTranslateY() < 0 || tail.getTranslateY() >= HEIGHT) {
                restartGame();
//                animation.stop();


            }

            if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {


                food.setTranslateX(randomWithRange(BLOCK_SIZE, WIDTH - BLOCK_SIZE));
                food.setTranslateY(randomWithRange(BLOCK_SIZE, HEIGHT - BLOCK_SIZE));

                Rectangle rectangle = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
                rectangle.setTranslateX(tail.getTranslateX());
                rectangle.setTranslateY(tail.getTranslateY());
                snakeList.add(rectangle);
            }
        });

        animation.getKeyFrames().add(frame);
        animation.setCycleCount(Timeline.INDEFINITE);

        root.getChildren().addAll(food, snakeBody);

        return root;
    }

    private int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int)((Math.random() * range) + min) / BLOCK_SIZE * BLOCK_SIZE;
    }

    public void restartGame() {
        stopGame();
        startGame();
    }

    public void startGame() {
        direction = Main.Direction.RIGHT;
        Rectangle head = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        snakeList.add(head);
        animation.play();
        isApplicationRunning = true;
    }

    public void stopGame() {
        isApplicationRunning = false;
        animation.stop();
        snakeList.clear();
    }


    public String gameOver;

    @Override
    public void start(Stage primaryStage) {

        try {
            Scene scene = new Scene(drawMethod());

            scene.setOnKeyPressed(event -> {
                if (! isMoved) {
                    return;
                }

                switch (event.getCode()) {
                    case UP:
                        if (direction != Direction.DOWN) {
                            direction = Direction.UP;
                        }
                        break;
                    case DOWN:
                        if (direction != Direction.UP) {
                            direction = Direction.DOWN;
                        }
                        break;
                    case LEFT:
                        if (direction != Direction.RIGHT) {
                            direction = Direction.LEFT;
                        }
                        break;
                    case RIGHT:
                        if (direction != Direction.LEFT) {
                            direction = Direction.RIGHT;
                        }
                        break;
                }

                isMoved = false;
            });

            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("Snake Game");
            this.primaryStage.setScene(scene);
            this.primaryStage.show();


            startGame();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}