package ir.ac.kntu;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static ir.ac.kntu.Player.PLAYER_ONE;
import static ir.ac.kntu.Player.PLAYER_TWO;
import static ir.ac.kntu.Tower.*;
import static ir.ac.kntu.Map.*;

public final class MouseClickHandler implements EventHandler {
    private Tower tower;
    private static ArrayList<MouseClickHandler> handlers = new ArrayList<>();
    private static MouseClickHandler handler = new MouseClickHandler();
    private static int towerCounter = 0;
    private static Player turn;

    private MouseClickHandler() {
    }

    public MouseClickHandler(Tower tower) {
        this.tower = tower;
        handlers.add(this);
    }

    public static void setTurn(Player turn) {
        MouseClickHandler.turn = turn;
    }

    public static MouseClickHandler getMouseClickHandler() {
        return handler;
    }

    //ToDo shorter
    @Override
    public void handle(Event event) {
        int numberOfEachTower = getNumberOfEachTower();
        int numberOfTowerKinds = getNumberOfTowerKinds();
        for (MouseClickHandler handler : handlers) {
            Rectangle clickedSquare = (Rectangle) event.getSource();
            if (clickedSquare.getFill() != Color.BLACK &&
                    handler.tower.getPosition() == null) {
                clickedSquare.setFill(Color.BLACK);
                if (towerCounter / numberOfEachTower == 0) {
                    FxClass.setImage(handler.tower.getImageView(),
                            clickedSquare, getBlackTowerPicPath());
                } else {
                    FxClass.setImage(handler.tower.getImageView(),
                            clickedSquare, getElecTowerPicPath());
                }
                towerCounter++;
                handler.tower.setPosition(clickedSquare);
                if (towerCounter == numberOfEachTower * numberOfTowerKinds) {
                    for (Rectangle square : getChosenMap().getSquares()) {
                        square.setOnMouseClicked(null);
                    }
                    if (turn == PLAYER_TWO) {
                        FxClass.getMessage().setText(null);
                        PLAYER_TWO.setTowersAreSet(true);
                        FxClass.getMessage().setText("All Towers are set.");
                        return;
                    }
                    handlers.clear();
                    towerCounter = 0;
                    PLAYER_ONE.setTowersAreSet(true);
                    setTowers(PLAYER_TWO);
                } else {
                    FxClass.getMessage().setText(turn.getName() +
                            ", click on the place you want to put " +
                            getTowerKinds()[towerCounter / numberOfEachTower] +
                            " " + (towerCounter % numberOfEachTower + 1));
                }
                return;
            }
        }
    }
}