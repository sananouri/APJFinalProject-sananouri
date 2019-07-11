package ir.ac.kntu;

import java.util.ArrayList;

import ir.ac.kntu.Soldier.*;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

import static ir.ac.kntu.FxClass.setImage;
import static ir.ac.kntu.Map.getChosenMap;
import static ir.ac.kntu.Soldier.newSoldier;
import static ir.ac.kntu.Tower.*;

public enum Player {
    PLAYER_ONE, PLAYER_TWO;
    private static final int NUMBER_OF_SOLDIER_KINDS = 4;
    private double energyIncreaseRate = 2;
    private double energy = 0;
    private String name = null;
    private int lives = 3;
    private KeyCode preLastPressedKey = null;
    private KeyCode lastPressedKey = null;
    private ArrayList<Soldier> soldiers = new ArrayList<>();
    private ArrayList<SoldierType> soldierTypes = new ArrayList<>();
    private ArrayList<Rectangle> startPoints = new ArrayList<>();
    private ArrayList<Rectangle> soldierMakerTowerPlaces = new ArrayList<>();
    private Tower[][] towers =
            new Tower[getNumberOfTowerKinds()][getNumberOfEachTower()];
    private Tower soldierMaker = null;
    private ArrayList<Tower> towerArrayList = new ArrayList<>();
    private boolean towersAreSet = false;
    private boolean towersShot = false;
    private boolean innerSoldiersShot;
    private boolean innerSoldiersMoved;
    private boolean outerSoldiersShot;
    private boolean outerSoldiersMoved;

    Player() {
        setTowers();
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setTowers() {
        for (int i = 0; i < getNumberOfTowerKinds(); i++) {
            for (int j = 0; j < getNumberOfEachTower(); j++) {
                towers[i][j] = newTower(getTowerKinds()[i]);
                towers[i][j].setOwner(this);
                towerArrayList.add(towers[i][j]);
            }
        }
    }

    public void setPreLastPressedKey(KeyCode preLastPressedKey) {
        this.preLastPressedKey = preLastPressedKey;
    }

    public void setLastPressedKey(KeyCode lastPressedKey) {
        this.lastPressedKey = lastPressedKey;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setEnergy(double energy) {
        if (energy <= 100) {
            this.energy = energy;
        }
    }

    public void setSoldierMaker(Tower soldierMaker) {
        this.soldierMaker = soldierMaker;
        this.soldierMaker.setPosition(soldierMakerTowerPlaces.get(0));
        this.soldierMaker.setOwner(this);
        setImage(this.soldierMaker.getImageView(),
                this.soldierMaker.getPosition(),
                getSoldierMakerTowerPath());
        energyIncreaseRate /= 4;
        this.towerArrayList.add(this.soldierMaker);
    }

    public void setTowersAreSet(boolean towersAreSet) {
        this.towersAreSet = towersAreSet;
    }

    public boolean towersAreSet() {
        return towersAreSet;
    }

    public boolean towersShot() {
        return towersShot;
    }

    public boolean innerSoldiersShot() {
        return innerSoldiersShot;
    }

    public boolean innerSoldiersMoved() {
        return innerSoldiersMoved;
    }

    public boolean outerSoldiersShot() {
        return outerSoldiersShot;
    }

    public boolean outerSoldiersMoved() {
        return outerSoldiersMoved;
    }

    public double getEnergy() {
        return energy;
    }

    public int getLives() {
        return lives;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Tower> getTowerArrayList() {
        return towerArrayList;
    }

    public ArrayList<Soldier> getSoldiers() {
        return soldiers;
    }

    public KeyCode getPreLastPressedKey() {
        return preLastPressedKey;
    }

    public ArrayList<SoldierType> getSoldierTypes() {
        return soldierTypes;
    }

    public Tower getSoldierMaker() {
        return soldierMaker;
    }

    public ArrayList<Card> getCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(soldiers);
        cards.addAll(towerArrayList);
        return cards;
    }

    public ArrayList<Rectangle> getStartPoints() {
        return startPoints;
    }

    public static int getNumberOfSoldierKinds() {
        return NUMBER_OF_SOLDIER_KINDS;
    }

    public Player getOpponent() {
        if (this == PLAYER_TWO) {
            return PLAYER_ONE;
        } else {
            return PLAYER_TWO;
        }
    }

    public double getEnergyIncreaseRate() {
        return energyIncreaseRate;
    }

    public ArrayList<Rectangle> getSoldierMakerTowerPlaces() {
        return soldierMakerTowerPlaces;
    }

    public void setTowerHandlers() {
        for (int i = 0; i < getNumberOfTowerKinds(); i++) {
            for (int j = 0; j < getNumberOfEachTower(); j++) {
                towers[i][j].setHandler(new MouseClickHandler(towers[i][j]));
            }
        }
    }

    public Thread getTowersShootThread() {
        return new Thread(() -> {
            towersShot = false;
            for (int i = 0; i < getNumberOfTowerKinds(); i++) {
                for (int j = 0; j < getNumberOfEachTower(); j++) {
                    towers[i][j].shoot();
                }
            }
            towersShot = true;
        });
    }

    public Thread getSoldiersShootThread(boolean inner) {
        return new Thread(() -> {
            if (inner) {
                innerSoldiersShot = false;
            } else {
                outerSoldiersShot = false;
            }
            double height = getChosenMap().getHeight();
            for (int i = 0; i < soldiers.size(); i++) {
                Rectangle position = soldiers.get(i).getPosition();
                if (this == PLAYER_ONE) {
                    if (inner && position.getY() < height / 2) {
                        soldiers.get(i).shoot();
                    }
                    if (!inner && position.getY() >= height / 2) {
                        soldiers.get(i).shoot();
                    }
                } else {
                    if (inner && position.getY() >= height / 2) {
                        soldiers.get(i).shoot();
                    }
                    if (!inner && position.getY() < height / 2) {
                        soldiers.get(i).shoot();
                    }
                }
            }
            if (inner) {
                innerSoldiersShot = true;
            } else {
                outerSoldiersShot = true;
            }
        });
    }

    public Thread getMoveInnerSoldiersThread() {
        return new Thread(() -> {
            innerSoldiersMoved = false;
            for (int i = 0; i < soldiers.size(); i++) {
                Rectangle position = soldiers.get(i).getPosition();
                if (this == PLAYER_ONE) {
                    if (position.getY() < getChosenMap().getHeight() / 2) {
                        soldiers.get(i).move();
                    }
                } else if (position.getY() >= getChosenMap().getHeight() / 2) {
                    soldiers.get(i).move();
                }
            }
            innerSoldiersMoved = true;
        });
    }

    public Thread getMoveOuterSoldiersThread() {
        return new Thread(() -> {
            outerSoldiersMoved = false;
            for (int i = 0; i < soldiers.size(); i++) {
                Rectangle position = soldiers.get(i).getPosition();
                if (this == PLAYER_ONE) {
                    if (position.getY() >= getChosenMap().getHeight() / 2) {
                        soldiers.get(i).move();
                    }
                } else if (position.getY() < getChosenMap().getHeight() / 2) {
                    soldiers.get(i).move();
                }
            }
            outerSoldiersMoved = true;
        });
    }

    private static Soldier handlePlayerOnePreLastKeyPress() {
        Soldier soldier;
        switch (PLAYER_ONE.preLastPressedKey) {
            case Q:
                soldier = newSoldier(PLAYER_ONE.soldierTypes.get(0));
                break;
            case W:
                soldier = newSoldier(PLAYER_ONE.soldierTypes.get(1));
                break;
            case E:
                soldier = newSoldier(PLAYER_ONE.soldierTypes.get(2));
                break;
            case R:
                soldier = newSoldier(PLAYER_ONE.soldierTypes.get(3));
                break;
            case Z:
                return null;
            default:
                PLAYER_ONE.preLastPressedKey = null;
                PLAYER_ONE.lastPressedKey = null;
                return null;
        }
        return soldier;
    }

    private static void handlePlayerOneLastKeyPress(Soldier soldier) {
        switch (PLAYER_ONE.lastPressedKey) {
            case NUMPAD1:
                if (PLAYER_ONE.preLastPressedKey == KeyCode.Z) {
                    if (PLAYER_ONE.soldierMaker == null) {
                        PLAYER_ONE.setSoldierMaker(
                                newTower(Tower.getTowerKinds()[2]));
                    }
                    return;
                }
                soldier.setPosition(PLAYER_ONE.startPoints.get(0));
                break;
            case NUMPAD2:
                soldier.setPosition(PLAYER_ONE.startPoints.get(1));
                break;
            case NUMPAD3:
                soldier.setPosition(PLAYER_ONE.startPoints.get(2));
                break;
            default:
                PLAYER_ONE.preLastPressedKey = null;
                PLAYER_ONE.lastPressedKey = null;
                return;
        }
        PLAYER_ONE.preLastPressedKey = null;
        PLAYER_ONE.lastPressedKey = null;
        if (PLAYER_ONE.getEnergy() < soldier.getEnergy() ||
                soldier.getPosition() == null) {
            return;
        }
        soldier.setOwner(PLAYER_ONE);
        PLAYER_ONE.soldiers.add(soldier);
        soldier.setImage();
    }

    public static void handlePlayerOneKeyPress() {
        Soldier soldier = handlePlayerOnePreLastKeyPress();
        if (PLAYER_ONE.lastPressedKey != null) {
            handlePlayerOneLastKeyPress(soldier);
        }
    }

    private static Soldier handlePlayerTwoPreLastKeyPress() {
        Soldier soldier;
        switch (PLAYER_TWO.preLastPressedKey) {
            case Y:
                soldier = newSoldier(PLAYER_TWO.soldierTypes.get(0));
                break;
            case U:
                soldier = newSoldier(PLAYER_TWO.soldierTypes.get(1));
                break;
            case I:
                soldier = newSoldier(PLAYER_TWO.soldierTypes.get(2));
                break;
            case O:
                soldier = newSoldier(PLAYER_TWO.soldierTypes.get(3));
                break;
            case N:
                return null;
            default:
                PLAYER_TWO.preLastPressedKey = null;
                PLAYER_TWO.lastPressedKey = null;
                return null;
        }
        return soldier;
    }

    private static void handlePlayerTwoLastKeyPress(Soldier soldier) {
        switch (PLAYER_TWO.lastPressedKey) {
            case NUMPAD6:
                if (PLAYER_TWO.preLastPressedKey == KeyCode.N) {
                    if (PLAYER_TWO.soldierMaker == null) {
                        PLAYER_TWO.setSoldierMaker(
                                newTower(Tower.getTowerKinds()[2]));
                    }
                    return;
                }
                soldier.setPosition(PLAYER_TWO.startPoints.get(0));
                break;
            case NUMPAD7:
                soldier.setPosition(PLAYER_TWO.startPoints.get(1));
                break;
            case NUMPAD8:
                soldier.setPosition(PLAYER_TWO.startPoints.get(2));
                break;
            default:
                PLAYER_TWO.preLastPressedKey = null;
                PLAYER_TWO.lastPressedKey = null;
                return;
        }
        PLAYER_TWO.preLastPressedKey = null;
        PLAYER_TWO.lastPressedKey = null;
        if (PLAYER_TWO.getEnergy() < soldier.getEnergy() ||
                soldier.getPosition() == null) {
            return;
        }
        soldier.setOwner(PLAYER_TWO);
        PLAYER_TWO.soldiers.add(soldier);
        soldier.setImage();
    }

    public static void handlePlayerTwoKeyPress() {
        Soldier soldier = handlePlayerTwoPreLastKeyPress();
        if (PLAYER_TWO.lastPressedKey != null) {
            handlePlayerTwoLastKeyPress(soldier);
        }
    }
}