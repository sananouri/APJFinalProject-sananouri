package ir.ac.kntu;

import static ir.ac.kntu.Map.*;

public class MainClass {

    public static void main(String[] args) {
        System.out.print("First Player: ");
        Player playerOne = Player.PLAYER_ONE;
        playerOne.setName(ScannerWrapper.getInstance().next());
        System.out.print("Second Player: ");
        Player playerTwo = Player.PLAYER_TWO;
        playerTwo.setName(ScannerWrapper.getInstance().next());
        Soldier.getSoldierKinds(playerOne);
        Soldier.getSoldierKinds(playerTwo);
        printMapsMenu();
        MapType mapType = scanMapType();
        Map map = getMap(mapType);
        while (map == null) {
            System.out.print("Invalid choice! Try again: ");
            mapType = scanMapType();
            map = getMap(mapType);
        }
        setChosenMap(map);
        ScannerWrapper.getInstance().close();
        FxClass.launchFxApplication(args);
    }
}