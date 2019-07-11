package ir.ac.kntu;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ir.ac.kntu.FxClass.setImage;
import static ir.ac.kntu.Map.getChosenMap;
import static ir.ac.kntu.Player.*;

public class CyclePlayer {
    private static int cycleCounter = 0;
    private static ArrayList<Card> deadCards = new ArrayList<>();

    public static ArrayList<Card> getDeadCards() {
        return deadCards;
    }

    public static void playCycle() {
        deadCards.clear();
        cycleCounter++;
        if (cycleCounter % 5 == 0) {
            useSoldierMakerTower(PLAYER_ONE);
            useSoldierMakerTower(PLAYER_TWO);
        }
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(PLAYER_ONE.getTowersShootThread());
        executor.execute(PLAYER_TWO.getTowersShootThread());
        while (!PLAYER_ONE.towersShot() || !PLAYER_TWO.towersShot()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor.execute(PLAYER_ONE.getSoldiersShootThread(true));
        executor.execute(PLAYER_TWO.getSoldiersShootThread(true));
        while (!PLAYER_ONE.innerSoldiersShot() ||
                !PLAYER_TWO.innerSoldiersShot()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor.execute(PLAYER_ONE.getSoldiersShootThread(false));
        executor.execute(PLAYER_TWO.getSoldiersShootThread(false));
        while (!PLAYER_ONE.outerSoldiersShot() ||
                !PLAYER_TWO.outerSoldiersShot()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor.execute(PLAYER_ONE.getMoveInnerSoldiersThread());
        executor.execute(PLAYER_TWO.getMoveInnerSoldiersThread());
        while (!PLAYER_ONE.innerSoldiersMoved() ||
                !PLAYER_TWO.innerSoldiersMoved()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor.execute(PLAYER_ONE.getMoveOuterSoldiersThread());
        executor.execute(PLAYER_TWO.getMoveOuterSoldiersThread());
        while (!PLAYER_ONE.outerSoldiersMoved() ||
                !PLAYER_TWO.outerSoldiersMoved()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        PLAYER_ONE.setEnergy(PLAYER_ONE.getEnergy() +
                PLAYER_ONE.getEnergyIncreaseRate());
        PLAYER_TWO.setEnergy(PLAYER_TWO.getEnergy() +
                PLAYER_TWO.getEnergyIncreaseRate());
        for (Card card : deadCards) {
            Rectangle position = card.getPosition();
            if (card instanceof Soldier) {
                Platform.runLater(() -> setImage(card.getImageView(), position,
                        Soldier.getDeadSoldierPicPath()));
            }
            if (card instanceof Tower) {
                Platform.runLater(() -> setImage(card.getImageView(), position,
                        Tower.getFallenTowerPicPath()));
            }
        }
    }

    public static void setLives(Player player) {
        double x, y;
        Player opponent = player.getOpponent();
        int numberOfStartPoints = getChosenMap().getNumberOfStartPoints();
        double[] startPointX = new double[numberOfStartPoints];
        double[] startPointY = new double[numberOfStartPoints];
        for (int i = 0; i < numberOfStartPoints; i++) {
            startPointX[i] = player.getStartPoints().get(i).getX();
            startPointY[i] = player.getStartPoints().get(i).getY();
        }
        ArrayList<Soldier> soldiers = new ArrayList<>();
        for (Soldier soldier : opponent.getSoldiers()) {
            x = soldier.getPosition().getX();
            y = soldier.getPosition().getY();
            for (int i = 0; i < numberOfStartPoints; i++) {
                if (x == startPointX[i] && y == startPointY[i]) {
                    soldiers.add(soldier);
                    player.setLives(player.getLives() - 1);
                    break;
                }
            }
        }
        opponent.getSoldiers().removeAll(soldiers);
    }

    private static void useSoldierMakerTower(Player player) {
        if (player.getSoldierMaker() == null) {
            return;
        }
        int numberOfStartPoints = getChosenMap().getNumberOfStartPoints();
        Random random = new Random();
        int startPointIndex = random.nextInt(numberOfStartPoints);
        Rectangle startPoint = player.getStartPoints().get(startPointIndex);
        int numberOfSoldierTypes = Player.getNumberOfSoldierKinds();
        int soldierTypeIndex = random.nextInt(numberOfSoldierTypes);
        Soldier.SoldierType type =
                player.getOpponent().getSoldierTypes().get(soldierTypeIndex);
        Soldier soldier = Soldier.newSoldier(type);
        soldier.setPosition(startPoint);
        if (soldier.getPosition() == null) {
            return;
        }
        soldier.setOwner(player);
        player.getSoldiers().add(soldier);
        soldier.setImage();
    }
}