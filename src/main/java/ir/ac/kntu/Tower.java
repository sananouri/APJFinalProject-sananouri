package ir.ac.kntu;

import javafx.scene.shape.Rectangle;

import static ir.ac.kntu.Map.*;
import static ir.ac.kntu.MouseClickHandler.getMouseClickHandler;
import static ir.ac.kntu.Player.*;

public final class Tower extends Card {
    private static final String BLACK_TOWER_PIC_PATH =
            "pics/blackTower.png";
    private static final String ELEC_TOWER_PIC_PATH =
            "pics/electricalTower.jpg";
    private static final String SOLDIER_MAKER_TOWER_PATH =
            "pics/soldierMaker.png";
    private static final String FALLEN_TOWER_PIC_PATH =
            "pics/fallenTower.jpg";
    private static final int NUMBER_OF_EACH_TOWER = 3;
    private MouseClickHandler handler;

    private enum TowerKind {BLACK_TOWER, ELECTRICAL_TOWER, SOLDIER_MAKER}

    public Tower(int lives, int damageRate, int energy, int range) {
        super(lives, energy, range, damageRate);
    }

    public Tower() {
    }

    public void setHandler(MouseClickHandler handler) {
        this.handler = handler;
    }

    public static void setTowers(Player player) {
        FxClass.getMessage().setText(player.getName() +
                ", click on the place you want to put " +
                Tower.getTowerKinds()[0] + " 1");
        player.setTowerHandlers();
        MouseClickHandler.setTurn(player);
        int mapHeight = getChosenMap().getHeight();
        double y;
        for (Rectangle square : getChosenMap().getTowerPlaces()) {
            y = square.getY();
            if (((y - 30) / 45 < mapHeight / 2 && player == PLAYER_ONE) ||
                    ((y - 30) / 45 >= mapHeight / 2 && player == PLAYER_TWO)) {
                square.setOnMouseClicked(getMouseClickHandler());
            }
        }
    }

    public static String getBlackTowerPicPath() {
        return BLACK_TOWER_PIC_PATH;
    }

    public static String getElecTowerPicPath() {
        return ELEC_TOWER_PIC_PATH;
    }

    public static int getNumberOfEachTower() {
        return NUMBER_OF_EACH_TOWER;
    }

    public static String getFallenTowerPicPath() {
        return FALLEN_TOWER_PIC_PATH;
    }

    public static int getNumberOfTowerKinds() {
        return TowerKind.values().length - 1;
    }

    public static TowerKind[] getTowerKinds() {
        return TowerKind.values();
    }

    public static String getSoldierMakerTowerPath() {
        return SOLDIER_MAKER_TOWER_PATH;
    }

    public static Tower newTower(TowerKind kind) {
        switch (kind) {
            case BLACK_TOWER:
                return new Tower(2000, 300, 40, 2);
            case ELECTRICAL_TOWER:
                return new Tower(1500, 250, 45, 3);
            case SOLDIER_MAKER:
                return new Tower(500, 0, 0, 0);
            default:
                return new Tower();
        }
    }
}