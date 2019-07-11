package ir.ac.kntu;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import static ir.ac.kntu.Map.getChosenMap;
import static ir.ac.kntu.Map.getSquareWidth;
import static ir.ac.kntu.Player.*;

public class Soldier extends Card {
    private static final String ARCHER_PIC_PATH = "pics/archer.jpg";
    private static final String DRAGON_PIC_PATH = "pics/dragon.jpg";
    private static final String GOBLIN_PIC_PATH = "pics/goblin.jpg";
    private static final String KNIGHT_PIC_PATH = "pics/knight.jpg";
    private static final String SHIELD_PIC_PATH = "pics/shield.jpg";
    private static final String SWORDSMAN_PIC_PATH = "pics/swordsman.jpg";
    private static final String DEAD_SOLDIER_PIC_PATH = "pics/deadSoldier.jpg";
    private final int maxSpeed;
    private SoldierType type;

    enum SoldierType {
        SWORDSMAN, GOBLIN, SHIELD, KNIGHT, ARCHER, DRAGON, UNDEFINED
    }

    public Soldier(int energy, int lives, int maxSpeed,
                   int damageRate, int range, SoldierType type) {
        super(lives, energy, range, damageRate);
        this.maxSpeed = maxSpeed;
        this.type = type;
    }

    private Soldier() {
        maxSpeed = 0;
    }

    public static String getDeadSoldierPicPath() {
        return DEAD_SOLDIER_PIC_PATH;
    }

    synchronized final void move() {
        Random random = new Random();
        int speed = random.nextInt(maxSpeed + 1);
        Rectangle position = getPosition();
        for (int i = 0; i < speed; i++) {
            position = getNextPlace(position, getOwner());
            if (position == null) {
                return;
            }
            for (Card card : getOwner().getOpponent().getCards()) {
                if (position.getY() == card.getPosition().getY()) {
                    if (position.getX() == card.getPosition().getY()) {
                        return;
                    }
                }
            }
        }
        for (Card card : Card.getCards()) {
            double x = card.getPosition().getX();
            double y = card.getPosition().getY();
            if (x == position.getX() && y == position.getY()) {
                return;
            }
        }
        setPosition(getChosenMap().getSquare(
                position.getX(), position.getY()));
        getImageView().relocate(position.getX(), position.getY());
    }

    private static Rectangle getNextPlace(Rectangle position, Player owner) {
        double currentX = position.getX();
        double currentY = position.getY();
        Rectangle nextPosition;
        double width = getSquareWidth();
        ArrayList<Rectangle> possiblePlaces = new ArrayList<>();
        nextPosition = new Rectangle(currentX + width, currentY, width, width);
        if (getChosenMap().soldierPlacesContain(nextPosition)) {
            possiblePlaces.add(nextPosition);
        }
        nextPosition = new Rectangle(currentX - width, currentY, width, width);
        if (getChosenMap().soldierPlacesContain(nextPosition)) {
            possiblePlaces.add(nextPosition);
        }
        if (owner == PLAYER_ONE) {
            nextPosition = new
                    Rectangle(currentX, currentY + width, width, width);
        } else {
            nextPosition = new
                    Rectangle(currentX, currentY - width, width, width);
        }
        if (getChosenMap().soldierPlacesContain(nextPosition)) {
            possiblePlaces.add(nextPosition);
        }
        Random random = new Random();
        if (possiblePlaces.size() <= 0) {
            return null;
        }
        return possiblePlaces.get(random.nextInt(possiblePlaces.size()));
    }

    public static void getSoldierKinds(Player player) {
        printSoldierKindsMenu(player);
        int counter = 0;
        SoldierType type;
        while (counter < Player.getNumberOfSoldierKinds()) {
            System.out.print("Soldier Type " + (counter + 1) + ": ");
            type = scanSoldierType();
            if (type == SoldierType.UNDEFINED) {
                System.out.println("Invalid choice!");
            } else if (player.getSoldierTypes().contains(type)) {
                System.out.println(player.getName() +
                        ", you have already chosen this type.");
            } else {
                player.getSoldierTypes().add(type);
                counter++;
            }
        }
    }

    public static void printSoldierKindsMenu(Player player) {
        System.out.println("***********************************");
        System.out.println(player.getName() + ", you can select " +
                Player.getNumberOfSoldierKinds() + " of these soldier types:");
        System.out.println("Melee Soldiers:");
        System.out.println("1-Swordsman");
        System.out.println("2-Goblin");
        System.out.println("3-Shield");
        System.out.println("4-Knight");
        System.out.println("Ranger Soldiers:");
        System.out.println("5-Archer");
        System.out.println("6-Dragon");
        System.out.println("***********************************");
    }

    public static SoldierType scanSoldierType() {
        SoldierType[] types = SoldierType.values();
        int input = ScannerWrapper.getInstance().nextInt();
        for (int i = 0; i < types.length; i++) {
            if (input == i + 1) {
                return types[i];
            }
        }
        return SoldierType.UNDEFINED;
    }

    public static Soldier newSoldier(SoldierType type) {
        switch (type) {
            case SWORDSMAN:
                return new MeleeSoldier(20, 500, 1, 350, type);
            case GOBLIN:
                return new MeleeSoldier(10, 200, 3, 250, type);
            case SHIELD:
                return new MeleeSoldier(10, 1000, 1, 150, type);
            case KNIGHT:
                return new MeleeSoldier(30, 600, 2, 400, type);
            case ARCHER:
                return new Ranger(15, 300, 1, 200, 2, type);
            case DRAGON:
                return new Ranger(35, 500, 2, 350, 3, type);
            default:
                return new Soldier();
        }
    }

    public final void setImage() {
        ImageView imageView = getImageView();
        switch (type) {
            case SWORDSMAN:
                Platform.runLater(() -> FxClass.setImage(imageView,
                        getPosition(), SWORDSMAN_PIC_PATH));
                break;
            case GOBLIN:
                Platform.runLater(() -> FxClass.setImage(imageView,
                        getPosition(), GOBLIN_PIC_PATH));
                break;
            case SHIELD:
                Platform.runLater(() -> FxClass.setImage(imageView,
                        getPosition(), SHIELD_PIC_PATH));
                break;
            case KNIGHT:
                Platform.runLater(() -> FxClass.setImage(imageView,
                        getPosition(), KNIGHT_PIC_PATH));
                break;
            case ARCHER:
                Platform.runLater(() -> FxClass.setImage(imageView,
                        getPosition(), ARCHER_PIC_PATH));
                break;
            case DRAGON:
                Platform.runLater(() -> FxClass.setImage(imageView,
                        getPosition(), DRAGON_PIC_PATH));
                break;
            default:
                break;
        }
    }
}