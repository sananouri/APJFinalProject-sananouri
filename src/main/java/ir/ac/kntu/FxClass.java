package ir.ac.kntu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static ir.ac.kntu.Map.*;
import static ir.ac.kntu.Player.*;

public final class FxClass extends Application {
    private static Pane pane = new Pane();
    private static Scene scene;
    private static Label message;

    public static Label getMessage() {
        return message;
    }

    @Override
    public void start(Stage stage) {
        scene = new Scene(pane,
                getSquareWidth() * getChosenMap().getWidth(),
                getSquareWidth() * getChosenMap().getHeight() + 30);
        stage.setScene(scene);
        stage.show();
        pane.getChildren().addAll(getChosenMap().getSquares());
        message = new Label();
        pane.getChildren().add(message);
        Tower.setTowers(PLAYER_ONE);
        starterThread().start();
    }

    private Thread starterThread() {
        return new Thread(() -> {
            while (!PLAYER_ONE.towersAreSet() || !PLAYER_TWO.towersAreSet()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            scene.setOnKeyPressed(event -> {
                if (PLAYER_ONE.getPreLastPressedKey() == null) {
                    PLAYER_ONE.setPreLastPressedKey(event.getCode());
                } else {
                    PLAYER_ONE.setLastPressedKey(event.getCode());
                }
                handlePlayerOneKeyPress();
                if (PLAYER_TWO.getPreLastPressedKey() == null) {
                    PLAYER_TWO.setPreLastPressedKey(event.getCode());
                } else {
                    PLAYER_TWO.setLastPressedKey(event.getCode());
                }
                handlePlayerTwoKeyPress();
            });
            startCyclePlayer();
        });
    }

    private void startCyclePlayer() {
        while (PLAYER_ONE.getLives() > 0 && PLAYER_TWO.getLives() > 0) {
            CyclePlayer.playCycle();
            CyclePlayer.setLives(PLAYER_ONE);
            CyclePlayer.setLives(PLAYER_TWO);
        }
        if (PLAYER_ONE.getLives() <= 0) {
            Platform.runLater(() ->
                    message.setText(PLAYER_TWO.getName() + " won!"));
        } else {
            Platform.runLater(() ->
                    message.setText(PLAYER_ONE.getName() + " won!"));
        }
        /*Platform.runLater(() -> {
            Button button = new Button("ok");
            button.resize(20, 20);
            pane.getChildren().add(button);
            button.relocate(100, 0);
            button.setOnMouseClicked(event -> {
                System.exit(0);
            });
        });*/
    }

    public static void setImage(ImageView iv, Rectangle r, String path) {
        iv.setFitHeight(Map.getSquareWidth());
        iv.setFitWidth(Map.getSquareWidth());
        Image image = null;
        try {
            image = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        iv.setImage(image);
        iv.relocate(r.getX(), r.getY());
        if (!pane.getChildren().contains(iv)) {
            pane.getChildren().add(iv);
        }
    }

    public static void launchFxApplication(String[] args) {
        launch(args);
    }
}