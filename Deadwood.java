import java.util.ArrayList;

public class Deadwood {
    private static SetUp setup = SetUp.setup;
    private static GameView gameView = GameView.gameView;

    private static ArrayList<PlayerController> players;
    private static PlayerController player;
    private static Set currSet;
    private static BoardController board;
    private static ArrayList<Scene> cardScenes;
    private static int maxDays;
    private static int activeScenes;
    private static int currDay;
    private static int playerCount;
    private static boolean hasMoved;
    private static boolean hasTaken;
    private static boolean hasWorked;
    private static boolean isWorking;
    private static boolean inOffice;
    private static boolean isTaking;
    private static boolean inSet;
    private static boolean isWrapped;
    private static boolean isMoving;
    private static boolean isUpgrading;
    private static int playerIndex = 0;

    public static void initializePlayers(int numPlayers) {
        playerCount = numPlayers;
        ArrayList<String> playerNames = new ArrayList<String>();
        playerNames.add("Blue");
        playerNames.add("Cyan");
        playerNames.add("Green");
        playerNames.add("Orange");
        playerNames.add("Pink");
        playerNames.add("Red");
        playerNames.add("Violet");
        playerNames.add("Yellow");
        players = setup.initializePlayers(playerCount, playerNames, board);
        player = players.get(playerIndex);
        resetBools();
    }

    public static void initialDisplay() { 
        GUIView.displayPlayerInfo(player.getPlayerName(), player.getPlayerRank(),
            player.getPlayerDollars(), player.getPlayerCredits(), player.getPlayerRehearsals());
        GUIView.displayCurrentDay(currDay);    
    }

    public static void main(String[] args) {
        // Handle Arguments
        double scale = 0.5;
        if (args.length > 0) {
            scale = Double.parseDouble(args[0]);
            if (scale < 0.35) {
                scale = 0.35;
            }
            if (scale > 3) {
                scale = 3;
            }
        }

        // Setup Game
        board = setup.initializeBoard();
        cardScenes = setup.initializeCards();
        setup.assignScenes(board, cardScenes);
        maxDays = setup.getMaxDays(playerCount);
        currDay = 1;
        activeScenes = 10;
        
        try {
            GUIView.launchApp(scale, board); // New GUI Implementation
        } catch (Exception e) {
            System.out.println("Error running application");
            e.printStackTrace();
        }
        
        System.exit(0); // Temporary
    }

    public static boolean verifyAction(ActionType action) {
        switch (action) {
            case MOVE:
                if (hasMoved) {
                    gameView.displayErrorMessage(ErrorType.ALREADY_MOVED);
                    return false;
                } else if (isWorking) {
                    gameView.displayErrorMessage(ErrorType.MOVE_WHILE_WORKING);
                    return false;
                } else if (hasWorked) {
                    gameView.displayErrorMessage(ErrorType.WORK_AND_MOVE);
                    return false;
                } else if (hasTaken) {
                    return false;
                } else {
                    return true;
                }
            case TAKE:
                if (isWorking) {
                    gameView.displayErrorMessage(ErrorType.TAKE_WHILE_WORKING);
                    return false;
                } else if (!inSet){
                    return false;
                } else if (isWrapped) {
                    return false;
                } else {
                    return true;
                }
            case ACT:
                if (hasWorked) {
                    gameView.displayErrorMessage(ErrorType.ALREADY_WORKED);
                    return false;
                } else if (!isWorking) {
                    gameView.displayErrorMessage(ErrorType.ACT_WHILE_NOT_WORKING);
                    return false;
                } else if (hasTaken) {
                    gameView.displayErrorMessage(ErrorType.TAKE_AND_WORK);
                    return false;
                } else if (isWrapped) {
                    return false;
                } else {
                    return true;
                }
            case REHEARSE:
                if (hasWorked) {
                    gameView.displayErrorMessage(ErrorType.ALREADY_WORKED);
                    return true;
                } else if (!isWorking) {
                    gameView.displayErrorMessage(ErrorType.REHEARSE_WHILE_NOT_WORKING);
                    return false;
                } else if (hasTaken) {
                    gameView.displayErrorMessage(ErrorType.TAKE_AND_WORK);
                    return false;
                } else if (isWrapped) {
                    // scene wrapped
                    return false;
                } else {
                    return true;
                }
            case UPGRADE:
                if (inOffice) {
                    return true;
                } else {
                    gameView.displayErrorMessage(ErrorType.NOT_AT_OFFICE);
                    return false;
                }
            case UPGRADING:
                if (isUpgrading) {
                    return true;
                } else {
                    return false;
                }
            case ROLE:
                if (isTaking) {
                    return true;
                } else {
                    return false;
                }
            case LOCATION:
                if (isMoving) {
                    return true;
                } else {
                    return false;
                }
            case END:
                return true;
        }
        return false;
    }

    public static void resetBools() {
        hasMoved = false;
        hasTaken = false;
        hasWorked = false;
        isWorking = player.getPlayerIsWorking();
        currSet = player.getSet(player.getPlayerLocation());
        if (currSet != null) {
            inSet = true;
            isWrapped = currSet.getIsWrapped();
        }
        isTaking = false;
        isMoving = false;
        isUpgrading = false;
    }

    public static void moveButtonClicked() {
        if (verifyAction(ActionType.MOVE) && !isWorking) {
            GUIView.clearHighlightUpgrades();
            ArrayList<Role> roles = player.getAvailableRoles();
            ArrayList<String> roleNames = new ArrayList<String>();
            ArrayList<Integer> roleRanks = new ArrayList<Integer>();
            for (int i = 0; i < roles.size(); i++) {
                roleNames.add(roles.get(i).getRoleName());
                roleRanks.add(roles.get(i).getRank());
            }
            GUIView.clearHighlightRoles(player.getPlayerLocation().getLocationName(), roleNames, roleRanks);
            GUIView.highlightLocations(player.getAdjacentLocationNames());
            isMoving = true;
        }
    }

    public static void locationClicked(Location location) {
        if (verifyAction(ActionType.LOCATION) && !hasMoved) {
            if (player.getPlayerLocation().getAdjacentLocations().contains(location)) {
                player.move(location);
                currSet = player.getSet(location);
                if (currSet != null) {
                    isWrapped = currSet.getIsWrapped();
                    if (!currSet.getScene().getVisible()) {
                        currSet.getScene().setVisible(true);
                        GUIView.revealSet(currSet);
                    }
                    inSet = true;
                }
                hasMoved = true;
                inOffice = player.getPlayerLocation().getLocationName().toLowerCase().equals("office");
            } else {
                return;
            }
        }
    }

    public static void takeButtonClicked() {
        if (verifyAction(ActionType.TAKE) && !hasTaken) {
            GUIView.clearHightlightLocations(player.getAdjacentLocationNames());
            ArrayList<Role> roles = player.getAvailableRoles();
            ArrayList<String> roleNames = new ArrayList<String>();
            ArrayList<Integer> roleRanks = new ArrayList<Integer>();
            for (int i = 0; i < roles.size(); i++) {
                roleNames.add(roles.get(i).getRoleName());
                roleRanks.add(roles.get(i).getRank());
            }
            if (!roles.isEmpty()) {
                GUIView.highlightRoles(player.getPlayerLocation().getLocationName(), roleNames, roleRanks);
            } else {
                System.out.println("No available roles to take");
            }
            isTaking = true;
        }
    }

    public static void roleClicked(Role role) {
        if (verifyAction(ActionType.ROLE) && !hasTaken) {
            player.take(role);
            hasTaken = true;
        }
    }

    public static void actButtonClicked() {
        if (verifyAction(ActionType.ACT) && !hasWorked) {
            player.act();
            hasWorked = true;
        }
    }

    public static void rehearseButtonClicked() {
        if (verifyAction(ActionType.REHEARSE) && !hasWorked) {
            player.rehearse();
            hasWorked = true;
        }
    }

    public static void upgradeButtonClicked() {
        System.out.println("sad");
        if (verifyAction(ActionType.UPGRADE)) {
            System.out.println("bad");
            player.upgrade();
            isUpgrading = true;
        }
    }

    public static void upgradeChoiceClicked(int rank, Currency currency) {
        if (verifyAction(ActionType.UPGRADING)) {
            player.upgradeChoice(rank, currency);
        }
    }

    public static void endTurnButtonClicked() {
        if (verifyAction(ActionType.END)) {
            player.clearHighlighting();
            GUIView.clearHighlightUpgrades();
            playerIndex++;
            if (playerIndex >= playerCount) {
                playerIndex = 0;
            }
            player = players.get(playerIndex);
            player.updatePlayerGUI();

            if (activeScenes == 1) {
                currDay++;
                if (currDay == maxDays) {
                    ArrayList<String> winners = ScoreCalculator.getWinners(players);
                    GUIView.displayWinners(winners);
                } else {
                    removeLastScene();
                    board.getBoardTrailer().resetPlayerLocations(players);
                    setup.assignScenes(board, cardScenes);
                    for (PlayerController p: players) {
                        p.fire();
                    }
                    GUIView.updatePlayerLocation(board.getBoardTrailer());
                    GUIView.resetBoard(board);
                    GUIView.displayCurrentDay(currDay);
                    unwrapAllScenes();
                    activeScenes = 10;
                }
            }
            resetBools();
        }
    }

    private static void unwrapAllScenes() {
        ArrayList<Set> sets = board.getBoardSets();
        for (int i = 0; i < sets.size(); i++) {
            sets.get(i).unwrapScene();
        }
    }

    public static void decrementActiveScenes() {
        activeScenes--;
    }

    public static void removeLastScene() {
        for (Set s: board.getBoardSets()) {
            if (s.getIsWrapped() == false) {
                GUIView.removeScene(s);
            }
        }
    }

}

// // display winners when the game is over
// ArrayList<String> winners = ScoreCalculator.getWinners(players);
// gameView.displayWinners(winners);