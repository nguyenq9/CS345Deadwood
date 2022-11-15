import java.util.ArrayList;

public class Deadwood {  
    private static ArrayList<PlayerController> players;
    private static BoardController board;
    private static int maxDays;
    private static ArrayList<Scene> cardScenes;
    private static int activeScenes;
    private static int currDay;
    private static GameView gameView = GameView.gameView;

    public static void main(String[] args) {
        // Make calls to Setup to initialize game components
        // players = SetUp.initializePlayers(8);
        // board = SetUp.initializeBoard();
        // maxDays = SetUp.getMaxDays(8);

        // Big loop to control turns
        boolean devmode = false;
        board = SetUp.initializeBoard();
        cardScenes = SetUp.initializeCards();
        SetUp.assignScenes(board, cardScenes);

        int playerCount = 0;
        if (args.length > 0) {
            playerCount = Integer.parseInt(args[0]);
        } else {
            playerCount = gameView.getPlayerCount();
        }
        if (args.length > 1) {
            if (args[1].equals("-devmode")) {
                devmode = true;
            }
        }
        
        maxDays = SetUp.getMaxDays(playerCount);
        ArrayList<String> playerNames = gameView.getPlayerNames(playerCount);

        players = SetUp.initializePlayers(playerCount, playerNames, board);
        if (devmode) {
            for(int i = 0; i < playerCount; i++) {
                players.get(i).setPlayerDollars(1000);
                players.get(i).setPlayerCredits(1000);
                players.get(i).setPlayerRank(6);
                players.get(i).setPlayerLocation(board.getBoardOffice());
            }
        }
        int playerIndex = 0;
        
        for (int i = 0; i < maxDays; i++) {
            currDay = i+1;
            activeScenes = board.getBoardSets().size();
            // game loop
            // action keywords: move, take, act, rehearse, upgrade, end
            // info keywords: where, who, where all, help
            gameView.displayCurrentDay(currDay);
            while (activeScenes > 1) {
                PlayerController player = players.get(playerIndex);
                ActionType action;
                gameView.displayTurn(player.getPlayerName());
                boolean hasMoved = false;
                boolean hasTaken = false;
                boolean hasWorked = false;
                do {
                    boolean isWorking = player.getPlayerIsWorking();
                    action = GameView.getPlayerInput();
                    switch (action) {
                        case HELP:
                            gameView.displayHelp();
                            break;
                        case MOVE:
                            if (hasMoved) {
                                gameView.displayErrorMessage(ErrorType.ALREADY_MOVED);
                            } else if (isWorking) {
                                gameView.displayErrorMessage(ErrorType.MOVE_WHILE_WORKING);
                                break;
                            } else if (hasWorked) {
                                gameView.displayErrorMessage(ErrorType.WORK_AND_MOVE);
                            } else {
                                hasMoved = player.move();
                            }
                            break;
                        case TAKE:
                            if (isWorking) {
                                gameView.displayErrorMessage(ErrorType.TAKE_WHILE_WORKING);
                            } else {
                                hasTaken = player.take();
                            }
                            break;
                        case ACT:
                            if (hasWorked) {
                                gameView.displayErrorMessage(ErrorType.ALREADY_WORKED);
                            } else if (!isWorking) {
                                gameView.displayErrorMessage(ErrorType.ACT_WHILE_NOT_WORKING);
                            } else if (hasTaken) {
                                gameView.displayErrorMessage(ErrorType.TAKE_AND_WORK);
                            } else {
                                hasWorked = player.act();
                            }
                            break;
                        case REHEARSE:
                            if (hasWorked) {
                                gameView.displayErrorMessage(ErrorType.ALREADY_WORKED);
                            } else if (!isWorking) {
                                gameView.displayErrorMessage(ErrorType.REHEARSE_WHILE_NOT_WORKING);
                            } else if (hasTaken) {
                                gameView.displayErrorMessage(ErrorType.TAKE_AND_WORK);
                            } else {
                                hasWorked = player.rehearse();
                            }
                            break;
                        case UPGRADE:
                            player.upgrade();
                            break;
                        case SCENE:
                            gameView.displaySetInfo(player.getPlayerLocation().getLocationName(), board);
                            break;
                        case WHERE:
                            gameView.displayCurrentLocation(players.get(playerIndex));
                            break;
                        case WHO:
                            gameView.displayCurrentDetails(players.get(playerIndex));
                            break;
                        case DETAILS:
                            gameView.displayAllDetails(players);
                            break;
                        case END:
                            break;
                        case WRAP:
                            activeScenes = 1;
                            break;
                    }
                } while (action != ActionType.END);

                // next player turn
                playerIndex++;
                if (playerIndex >= playerCount) {
                    playerIndex = 0;
                }
            }
            board.getBoardTrailer().resetPlayerLocations(players);
            gameView.displayEndDay(currDay);
        }
        gameView.displayWinners(players);
    }

    public static void decrementActiveScenes() {
        activeScenes--;
    }
}