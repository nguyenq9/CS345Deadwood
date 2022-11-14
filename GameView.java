import java.util.Scanner;
import java.util.ArrayList;

public class GameView {

    public static GameView gameView = new GameView();

    private GameView() {

    }

    private static Scanner input = new Scanner(System.in);

    private void displayDetails(PlayerController player) {
        int rank = player.getPlayerRank();
        int dollars = player.getPlayerDollars();
        int credits = player.getPlayerCredits();
        int pracChip = player.getPlayerRehearsals();
        Location location = player.getPlayerLocation();
        String playerLocation = location.getLocationName();
        Role role = player.getPlayerRole();
        System.out.printf("Their current rank is %d, they have %d dollars, %d credits, and %d rehearsals. They are currently located in %s.\n",
            rank, dollars, credits, pracChip, playerLocation);
        if (role != null) {
            String roleName = role.getRoleName();
            String roleLine = role.getRoleLine();
            if (role.getOnCard()){
                System.out.printf("They are working %s, \"%s\" (on card)\n", roleName, roleLine);
            } else {
                System.out.printf("They are working %s, \"%s\" (off card)\n", roleName, roleLine);
            }
        }
    }

    public void displayTurn(String playerName) {
        System.out.println(playerName + "! It's your turn!");
    }
    
    public void displayHelp() {
        System.out.println("Possible commands: move, take, act, rehearse, upgrade, end, where, who, details");
    }

    public void displaySetInfo(String setName, BoardController board) {
        ArrayList<Set> sets = board.getBoardSets();
        Set set = null;
        for (int i = 0; i < sets.size(); i++) {
            if (sets.get(i).getLocationName().equals(setName)) {
                set = sets.get(i);
                break;
            }
        }
        if (set == null) return;
        Scene scene = set.getScene();
        System.out.println(set.getLocationName() + " has " + set.getShotCounters() + " shots remaining");
        System.out.println("The current scene is " + scene.getSceneName() + ": " + scene.getSceneDescription());
        System.out.println("The budget is " + scene.getBudget() + " million");
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

    public void displayMoveOption(ArrayList<String> options) {
        for (int i = 0; i < options.size(); i++) {
            System.out.println(" - " + options.get(i));
        }
    }

    public void displayRoleOptions(ArrayList<String> options, ArrayList<Integer> rank, ArrayList<Boolean> onCard) {
        for (int i = 0; i < options.size(); i++) {
            if (onCard.get(i).booleanValue()) {
                System.out.println(" - " + options.get(i) + " [Rank " + rank.get(i).intValue() + "] [On-Card]");
            } else {
                System.out.println(" - " + options.get(i) + " [Rank " + rank.get(i).intValue() + "] [Off-Card]");
            }
        }
    }

    public void displayMovingOutcome(String playerName, String locationName) {
        System.out.println(playerName + " moved to " + locationName + ".");
    }

    public void displayMovingFail() {
        System.out.println("Can't move. Make sure you spelled the name of the location correctly.");
    }

    public void displayTakeRoleOutcome(String playerName, String roleName) {
        System.out.println(playerName + " took the " + roleName + " role.");
    }

    public void displayTakingRoleFail() {
        System.out.println("Can't take the role. Make sure you spelled the name of the role correctly.");
    }

    public void displayActingOutcome(String roleLine, boolean success) {
        System.out.println("\"" + roleLine + "\"");
        if (success) {
            System.out.println("Acting success!");
        } else {
            System.out.println("Acting failed :(");
        }
    }
    
    public void displayActRoll(int roll) {
        System.out.println("Rolled " + roll);
    }

    public void displayNotAtOffice() {
        System.out.println("Can't upgrade when not in the casting office.");
    }

    public void displaySceneWrapped() {
        System.out.println("Last take complete! Distributing bonuses.");
    }

    public void displayRehearsingOutcome(String roleLine, String playerName) {
        System.out.println("\"" + roleLine + "\"");
        System.out.println(playerName + " gained a rehearsal token!");
    }

    public void displayUpgradeOptions(PlayerController player, BoardController board) {
        int rank = player.getPlayerRank();
        CastingOffice office = board.getBoardOffice();
        int dollars = 0, credits = 0;
        System.out.printf("You are currently rank %d. Costs to upgrade: \n", rank);
        for (int i = rank - 1; i < (office.getUpgradeDollarCosts().length); i++) {
            dollars = office.getUpgradeDollarCosts()[i];
            credits = office.getUpgradeCreditCosts()[i];
            System.out.printf("Rank: %d, Dollar Cost: %d, Credit Cost: %d\n", i + 2, dollars, credits);
        }
    }

    public void displayUpgradeFail() {
        System.out.println("Failed to upgrade. Make sure to enter a valid rank and that you have sufficient funds.");
    }

    public void displayUpgradeSuccess(int rank) {
        System.out.println("Successfully upgraded to rank " + rank + ".");
    }

    public void displayAlreadyMoved() {
        System.out.println("You have already moved this turn. Wait until next turn to move again.");
    }

    public void displayAlreadyWorked() {
        System.out.println("You have already worked this turn. Wait until next turn to work again.");
    }

    public void displayCantMoveWhileWorking() {
        System.out.println("You cannot move while you're working.");
    }

    public void displayCantTakeRoleWhileWorking() {
        System.out.println("You cannot take a role while you're working.");
    }

    public void displayCantTakeAndWork() {
        System.out.println("You cannot take a role and act on it on the same turn.")
    }

    public void displayCantActWhileNotWorking() {
        System.out.println("You cannot act unless you're working, take a role first.");
    }

    public void displayCantRehearseWhileNotWorking() {
        System.out.println("You cannot rehearse unless you're working, take a role first.");
    }

    public void displayCantTakeRole() {
        System.out.println("There are no roles to take here. Try another location.");
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
            case "set":
                return ActionType.SCENE;
            case "scene":
                return ActionType.SCENE;
            case "current":
                return ActionType.WHO;
            case "active":
                return ActionType.WHO;
            case "where":
                return ActionType.WHERE;
            case "whereall":
                return ActionType.DETAILS;
            case "details":
                return ActionType.DETAILS;
            case "info":
                return ActionType.DETAILS;
            default:
                return ActionType.HELP;
        }
    }

    public String getMoveOption() {
        System.out.println("Where do you want to move to?");
        System.out.print("> ");
        String adj = input.nextLine();
        adj = adj.toLowerCase();
        return adj;
    }

    public String getTakeRoleOption() {
        System.out.println("Which role do you want to take?");
        System.out.print("> ");
        String role = input.nextLine();
        role = role.toLowerCase();
        return role;
    }

    public int getUpgradeOption() {
        System.out.println("Which rank do you want to upgrade to?");
        System.out.print("> ");
        int rank = input.nextInt();
        input.nextLine();
        return rank;
    }

    public String getUpgradeCurrency() {
        System.out.println("How do you want to pay for this upgrade?");
        System.out.print("> ");
        String currency = input.nextLine();
        return currency;
    }

}