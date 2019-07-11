package ir.ac.kntu;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import static ir.ac.kntu.CyclePlayer.getDeadCards;
import static ir.ac.kntu.Player.*;

public class Card {
    private int lives;
    private int energy;
    private int range;
    private int damageRate;
    private Rectangle position;
    private Player owner;
    private ImageView imageView;
    private ArrayList<Card> possibleShots = new ArrayList<>();

    public Card(int lives, int energy, int range, int damageRate) {
        this.lives = lives;
        this.energy = energy;
        this.range = range;
        this.damageRate = damageRate;
        imageView = new ImageView();
    }

    public Card() {
    }

    public final void setPosition(Rectangle position) {
        if (this instanceof Tower) {
            this.position = position;
            return;
        }
        for (Card card : getCards()) {
            if (position.getY() == card.getPosition().getY()) {
                if (position.getX() == card.getPosition().getY()) {
                    this.position = null;
                }
            }
        }
        this.position = position;
    }

    public final void setOwner(Player owner) {
        this.owner = owner;
    }

    public final int getEnergy() {
        return energy;
    }

    public final Player getOwner() {
        return owner;
    }

    public final ImageView getImageView() {
        return imageView;
    }

    public final Rectangle getPosition() {
        return position;
    }

    public static ArrayList<Card> getCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(PLAYER_ONE.getCards());
        cards.addAll(PLAYER_TWO.getCards());
        return cards;
    }

    public final void addToPossibleShots(Card card) {
        possibleShots.add(card);
    }

    /*
     * to find positions within the range of a card
     * that contains a card of the opponent and add
     * it to possible shots.
     */
    public void findPossibleShots() {
        double xRangeRadius, yRangeRadius, x, y;
        boolean xIsInXRange, yIsInYRange;
        Player opponent = owner.getOpponent();
        xRangeRadius = range * Map.getSquareWidth();
        yRangeRadius = range * Map.getSquareWidth();
        for (Card card : opponent.getCards()) {
            x = card.getPosition().getX();
            y = card.getPosition().getY();
            xIsInXRange = x >= position.getX() - xRangeRadius &&
                    x <= position.getX() + xRangeRadius;
            yIsInYRange = y >= position.getY() - yRangeRadius &&
                    y <= position.getY() + yRangeRadius;
            if (xIsInXRange && yIsInYRange) {
                possibleShots.add(card);
            }
        }
    }

    synchronized final void shoot() {
        findPossibleShots();
        if (possibleShots.size() <= 0) {
            return;
        }
        Random random = new Random();
        int num = random.nextInt(possibleShots.size());
        Card shot = possibleShots.get(num);
        System.out.println("shot " + shot.position);
        System.out.println("shooter " + position);
        shot.lives -= damageRate;
        System.out.println(shot.lives);
        if (shot.lives <= 0) {
            getDeadCards().add(shot);
            if (shot instanceof Tower) {
                shot.owner.getTowerArrayList().remove(shot);
            }
            if (shot instanceof Soldier) {
                shot.owner.getSoldiers().remove(shot);
            }
            System.out.println("shot dead");
            shot.getImageView().setImage(null);
        }
        possibleShots.clear();
    }
}