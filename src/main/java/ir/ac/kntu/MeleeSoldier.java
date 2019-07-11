package ir.ac.kntu;

import static ir.ac.kntu.Map.getSquareWidth;

public final class MeleeSoldier extends Soldier {
    private static final int RANGE = 1;

    public MeleeSoldier(int energy, int lives, int maxSpeed,
                        int damageRate, SoldierType type) {
        super(energy, lives, maxSpeed, damageRate, RANGE, type);
    }

    @Override
    public void findPossibleShots() {
        double x, y;
        Player opponent = getOwner().getOpponent();
        for (Card card : opponent.getCards()) {
            x = card.getPosition().getX();
            y = card.getPosition().getY();
            if (y == getPosition().getY() - RANGE * getSquareWidth()) {
                if (x == getPosition().getX()) {
                    addToPossibleShots(card);
                }
            }
            if (x == getPosition().getX() + RANGE * getSquareWidth()) {
                if (y == getPosition().getY()) {
                    addToPossibleShots(card);
                }
            }
            if (y == getPosition().getY() + RANGE * getSquareWidth()) {
                if (x == getPosition().getX()) {
                    addToPossibleShots(card);
                }
            }
            if (x == getPosition().getX() - RANGE * getSquareWidth()) {
                if (y == getPosition().getY()) {
                    addToPossibleShots(card);
                }
            }
        }
    }
}