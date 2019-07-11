package ir.ac.kntu;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static ir.ac.kntu.Player.*;

public final class Map {
    private static final double SQUARE_WIDTH = 45;
    private static Map chosenMap;
    private final int width;
    private final int height;
    private final int numberOfStartPoints;
    private final String fileName;
    private ArrayList<Rectangle> soldierPlaces = new ArrayList<>();
    private ArrayList<Rectangle> towerPlaces = new ArrayList<>();
    private ArrayList<Rectangle> squares = new ArrayList<>();

    enum MapType {DEFAULT_MAP, UNDEFINED}

    public Map(int width, int height,
               int numberOfStartPoints, String fileName) {
        this.width = width;
        this.height = height;
        this.numberOfStartPoints = numberOfStartPoints;
        this.fileName = fileName;
        readMap();
    }

    public static void setChosenMap(Map chosenMap) {
        Map.chosenMap = chosenMap;
    }

    public static Map getChosenMap() {
        return chosenMap;
    }

    public ArrayList<Rectangle> getSquares() {
        return squares;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNumberOfStartPoints() {
        return numberOfStartPoints;
    }

    public static double getSquareWidth() {
        return SQUARE_WIDTH;
    }

    public ArrayList<Rectangle> getTowerPlaces() {
        return towerPlaces;
    }

    public Rectangle getSquare(double x, double y) {
        for (Rectangle square : squares) {
            if (square.getX() == x && square.getY() == y) {
                return square;
            }
        }
        return null;
    }

    public boolean soldierPlacesContain(Rectangle r) {
        for (Rectangle rectangle : soldierPlaces) {
            if (r.getY() == rectangle.getY() && r.getX() == rectangle.getX()) {
                return true;
            }
        }
        return false;
    }

    private void readMap() {
        String line;
        File file = new File(fileName);
        try (BufferedReader bufferedReader =
                     new BufferedReader(new FileReader(file))) {
            for (int i = 0; i < getHeight(); i++) {
                line = bufferedReader.readLine();
                String[] s = line.split(" ");
                setSquaresOfLine(s, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSquaresOfLine(String[] s, int lineNumber) {
        double x, y;
        Rectangle square;
        for (int i = 0; i < s.length; i++) {
            x = i * SQUARE_WIDTH;
            y = lineNumber * SQUARE_WIDTH + 30;
            square = new Rectangle(x, y, SQUARE_WIDTH, SQUARE_WIDTH);
            square.setStroke(Color.BLACK);
            switch (s[i]) {
                case "R":
                    square.setFill(Color.RED);
                    if (lineNumber < height / 2) {
                        PLAYER_ONE.getStartPoints().add(square);
                    } else {
                        PLAYER_TWO.getStartPoints().add(square);
                    }
                    soldierPlaces.add(square);
                    break;
                case "G":
                    square.setFill(Color.GREEN);
                    if (lineNumber < height / 2) {
                        PLAYER_ONE.getSoldierMakerTowerPlaces().add(square);
                    } else {
                        PLAYER_TWO.getSoldierMakerTowerPlaces().add(square);
                    }
                    break;
                case "B":
                    square.setFill(Color.BLUE);
                    towerPlaces.add(square);
                    break;
                case "Y":
                    square.setFill(Color.YELLOW);
                    soldierPlaces.add(square);
                    break;
                case "LG":
                    square.setFill(Color.LIGHTGRAY);
                    break;
                case "DG":
                    square.setFill(Color.DARKGRAY);
                    break;
                default:
                    break;
            }
            squares.add(square);
        }
    }

    public static void printMapsMenu() {
        System.out.println("***********************************");
        System.out.println("Available maps:");
        System.out.println("1- size = 20*20, 3 start points.");
        System.out.println("***********************************");
        System.out.print("\rPlease select your choice: ");
    }

    public static MapType scanMapType() {
        MapType[] types = MapType.values();
        int input = ScannerWrapper.getInstance().nextInt();
        for (int i = 0; i < types.length; i++) {
            if (input - 1 == i) {
                return types[i];
            }
        }
        return MapType.UNDEFINED;
    }

    public static Map getMap(MapType mapType) {
        switch (mapType) {
            case DEFAULT_MAP:
                return new Map(20, 20, 3, "maps\\DefaultMap.txt");
            default:
                return null;
        }
    }
}