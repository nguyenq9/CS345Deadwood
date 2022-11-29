import java.util.Scanner;
import java.util.ArrayList;

public class GameView {

    public static GameView gameView = new GameView();

    private GameView() {

    }

    private static Scanner input = new Scanner(System.in);

    // GENERAL INFO DISPLAY - - - - -

    public void displayDetails(String name, int rank, int dollars, int credits, int rehearsals, String playerLocation, Role role) {
        System.out.printf("%s's current rank is %d, they have %d dollars, %d credits, and %d rehearsals. They are currently located in %s.\n",
            name, rank, dollars, credits, rehearsals, playerLocation);
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
        System.out.println("");
        System.out.println(playerName + "! It's your turn!");
    }
    
    public void displayHelp() {
        System.out.println("Possible commands: [move], [take], [act], [rehearse], [upgrade], [set], end, [where], [who], [details]");
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

    public void displayCurrentLocation(String locationName) {
        System.out.println("The current player's location is " + locationName + ".");
    }

    public void displayCurrentDetails(String name, int rank, int dollars, int credits, int rehearsals, String playerLocation, Role role) {
        System.out.println("The current player is " + name + ".");
        displayDetails(name, rank, dollars, credits, rehearsals, playerLocation, role);
    }

    // ACTION OPTIONS DISPLAY - - - - - 

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

    public void displayUpgradeOptions(int playerRank, int[] dollarCosts, int[] creditCosts) {
        int dollars = 0, credits = 0;
        System.out.printf("You are currently rank %d. Costs to upgrade: \n", playerRank);
        for (int i = playerRank - 1; i < dollarCosts.length; i++) {
            dollars = dollarCosts[i];
            credits = creditCosts[i];
            System.out.printf("Rank: %d, Dollar Cost: %d, Credit Cost: %d\n", i + 2, dollars, credits);
        }
    }

    // ACTION OUTCOMES DISPLAY - - - - -

    public void displayMovingOutcome(String playerName, String locationName) {
        System.out.println(playerName + " moved to " + locationName + ".");
    }

    public void displayTakeRoleOutcome(String playerName, String roleName) {
        System.out.println(playerName + " took the " + roleName + " role.");
    }

    public void displayUpgradeOutcome(int rank) {
        System.out.println("Successfully upgraded to rank " + rank + ".");
    }

    public void displayActingOutcome(String roleLine, boolean success) {
        System.out.println("\"" + roleLine + "\"");
        if (success) {
            System.out.println("Acting success!");
        } else {
            System.out.println("Acting failed.");
        }
    }
    
    public void displayActRoll(int roll) {
        System.out.println("Rolled a " + roll);
    }

    public void displayRehearsingOutcome(String roleLine, String playerName) {
        System.out.println("\"" + roleLine + "\"");
        System.out.println(playerName + " gained a rehearsal token!");
    }

    public void displaySceneWrapped() {
        System.out.println("Last take complete!");
    }

    public void displayBonus() {
        System.out.println("Distributing Bonuses.");
    }

    public void displayDollarEarnings(String playerName, int amount) {
        if (amount == 1) {
            System.out.println(playerName + " earned a dollar!");
        } else {
            System.out.println(playerName + " earned " + amount + " dollars!");
        }
    }

    public void displayCreditEarnings(String playerName, int amount) {
        if (amount == 1) {
            System.out.println(playerName + " earned a credit!");
        } else {
            System.out.println(playerName + " earned " + amount + " credits!");
        }
    }

    public void displayWinners(ArrayList<String> winnerNames) {
        
        System.out.println("All days have been completed and the game is finished.");
        if (winnerNames.size() > 1) {
            System.out.println("The winners are: ");
        } else {
            System.out.println("The winner is: ");
        }

        for (int i = 0; i < winnerNames.size(); i++) {
            System.out.println(winnerNames.get(i) + "!");
        }
    }

    public void displayCurrentDay(int currDay) {
        System.out.println("===== Day " + currDay + " has started. =====");
    }

    public void displayEndDay(int currDay) {
        System.out.println("===== Day " + currDay + " has ended. =====");
    }

    // ERROR DISPLAY - - - - -

    public void displayErrorMessage(ErrorType errorType) {
        String errorMessage = "";
        switch(errorType) {
            case INVALID_LOCATION:
                errorMessage = "Failed to move. Make sure you spelled the name of the location correctly.";
                break;
            case INVALID_ROLE:
                errorMessage = "Failed to take a role. Make sure you spelled the name of the role correctly.";
                break;
            case INVALID_RANK:
                errorMessage = "Failed to upgrade. Make sure to enter a valid rank and that you have sufficient funds.";
                break;
            case NOT_AT_OFFICE:
                errorMessage = "You can't upgrade when not in the casting office.";
                break;
            case NO_ROLES:
                errorMessage = "There are no available roles at this location. Try somewhere else.";
                break;
            case ALREADY_MOVED:
                errorMessage = "You have already moved this turn. Wait until next turn to move again.";
                break;
            case ALREADY_WORKED:
                errorMessage = "You have already worked this turn. Wait until next turn to work again.";
                break;
            case MOVE_WHILE_WORKING:
                errorMessage = "You cannot move while you're working on a role.";
                break;
            case TAKE_WHILE_WORKING:
                errorMessage = "You cannot take a role while you're working on a role.";
                break;
            case WORK_AND_MOVE:
                errorMessage = "You cannot work and move on the same turn.";
                break;
            case TAKE_AND_WORK:
                errorMessage = "You cannot take a role and work on it on the same turn.";
                break;
            case ACT_WHILE_NOT_WORKING:
                errorMessage = "You cannot act on a role unless you're working on one, take a role first.";
                break;
            case REHEARSE_WHILE_NOT_WORKING:
                errorMessage = "You cannot rehearse a role unless you're working on one, take a role first.";
                break;
        }
        System.out.println("Error: " + errorMessage + " (This did not take up your turn.)");
    }

    // USER PROMPT DISPLAY - - - - -

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
            System.out.print("Enter Player " + (i + 1) + " Name: ");
            playerNames.add(input.nextLine());
        }
        return playerNames;
    }

    public static ActionType getPlayerInput() {
        System.out.print("> ");
        String action = input.nextLine().toLowerCase();
        switch(action) {
            // case "help":
            //     return ActionType.HELP;
            // case "?":
            //     return ActionType.HELP;
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
            // case "who":
            //     return ActionType.WHO;
            // case "current":
            //     return ActionType.WHO;
            // case "active":
            //     return ActionType.WHO;
            // case "set":
            //     return ActionType.SCENE;
            // case "scene":
            //     return ActionType.SCENE;
            // case "where":
            //     return ActionType.WHERE;
            // case "whoall":
            //     return ActionType.DETAILS;
            // case "details":
            //     return ActionType.DETAILS;
            // case "info":
            //     return ActionType.DETAILS;
            default:
                return ActionType.END;
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
        try {
            String rankInput = input.nextLine();
            int rank = Integer.parseInt(rankInput);
            return rank;
        } catch (Exception e) {
            return 0;
        }
        
    }

    public String getUpgradeCurrency() {
        System.out.println("How do you want to pay for this upgrade?");
        System.out.print("> ");
        String currency = input.nextLine();
        return currency;
    }
}