import java.util.Scanner;
import java.util.ArrayList;

public class GameView {

    public static GameView gameView = new GameView();

    private GameView() {

    }

    private static Scanner input = new Scanner(System.in);
    BoardController boardController;

    private void displayDetails(PlayerController player) {
        String name = player.getPlayerName();
        int dollars = player.getPlayerDollars();
        int credits = player.getPlayerCredits();
        int pracChip = player.getPlayerRehearsals();
        Location location = player.getPlayerLocation();
        String playerLocation = location.getLocationName();
        Role role = player.getPlayerRole();
        System.out.printf("They have %d dollars, %d credits, and %d rehearsals. They are currently located in %s.\n",
            dollars, credits, pracChip, playerLocation);
        if (role != null) {
            String roleName = role.getRoleName();
            String roleLine = role.getRoleLine();
            System.out.printf("They are working %s, \"%s\"\n", roleName, roleLine);
        }
    }
    
    public void displayHelp() {
        System.out.println("Possible commands: move, take, act, rehearse, upgrade, end, where, who, details.");
    }

    public void displayCurrentLocation(PlayerController player) {
        Location location = player.getPlayerLocation();
        System.out.println("The current player's location is " + location.getLocationName() + ".");
    }

    public void displayCurrentDetails(PlayerController player) {
        System.out.println("The current player is " + player.getPlayerName() + ".");
        displayDetails(player);
    }

    public void displayAllDetails(ArrayList<PlayerController> players) {
        int numPlayers = players.size();
        for (int i = 0; i < numPlayers; i++) {
            System.out.print(players.get(i).getPlayerName() + "'s Details: ");
            displayDetails(players.get(i));
        }
    }

    public void displayActingFail() {
        System.out.println("Can't act. Make sure you have a role and are able to work on it.");
    }

    public void displayActingSuccess(boolean success) {
        if (success) {
            System.out.println("Success!");
        } else {
            System.out.println("Acting failed :(");
        }
    }

    public int getPlayerCount() {
        int playerCount = input.nextInt();
        if (playerCount < 2) {
            playerCount = 2;
        }
        if (playerCount > 8) {
            playerCount = 8;
        }
        return playerCount;
    }

    public ArrayList<String> getPlayerNames(int playerCount) {
        ArrayList<String> playerNames = new ArrayList<String>();
        for (int i = 0; i < playerCount; i++) {
            System.out.println("Enter Player " + (i + 1) + " Name: ");
            playerNames.add(input.nextLine());
        }
        return playerNames;
    }

    // action keywords: move, take, act, rehearse, upgrade, end
    // info keywords: help, who, where, details

    public static ActionType getPlayerInput() {
        System.out.print("> ");
        String action = input.nextLine().toLowerCase();
        switch(action) {
            case "help":
                return ActionType.HELP;
            case "?":
                return ActionType.HELP;
            case "move":
                return ActionType.MOVE;
            case "take":
                return ActionType.TAKE;
            case "act":
                return ActionType.ACT;
            case "rehearse":
                return ActionType.REHEARSE;
            case "upgrade":
                return ActionType.UPGRADE;
            case "end":
                return ActionType.END;
            case "who":
                return ActionType.WHO;
            case "current":
                return ActionType.WHO;
            case "where":
                return ActionType.WHERE;
            case "details":
                return ActionType.DETAILS;
            case "info":
                return ActionType.DETAILS;
            default:
                return ActionType.HELP;
        }
    }

    public String getMoveOption() {
        String adj = input.nextLine();
        adj.toLowerCase();
        return adj;
    }

}