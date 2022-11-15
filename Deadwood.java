import java.util.ArrayList;

public class Deadwood {  
    private static ArrayList<PlayerController> players;
    private static BoardController board;
    private static SetUp setup = SetUp.setup;
    private static int maxDays;
    private static ArrayList<Scene> cardScenes;
    private static int activeScenes;
    private static int currDay;
    private static GameView gameView = GameView.gameView;

    public static void main(String[] args) {
        boolean devmode = false;
        board = setup.initializeBoard();
        cardScenes = setup.initializeCards();
        setup.assignScenes(board, cardScenes);

        int playerCount = 0;
        if (args.length > 0) {
            playerCount = Integer.parseInt(args[0]);
        } else {
            System.out.println("ERROR: Please include the number of players, Eg. \"java Deadwood 4\"");
            System.exit(1);
        }
        if (args.length > 1) {
            if (args[1].equals("-devmode")) {
                devmode = true;
            }
        }
        
        maxDays = setup.getMaxDays(playerCount);
        ArrayList<String> playerNames = gameView.getPlayerNames(playerCount);

        players = setup.initializePlayers(playerCount, playerNames, board);
        if (devmode) {
            for(int i = 0; i < playerCount; i++) {
                players.get(i).setPlayerDollars(1000);
                players.get(i).setPlayerCredits(1000);
                players.get(i).setPlayerRank(4);
                players.get(i).setPlayerLocation(board.getBoardOffice());
            }
        }
        int playerIndex = 0;
        
        // main game loop
        for (int i = 0; i < maxDays; i++) {
            currDay = i+1;
            activeScenes = board.getBoardSets().size();
            gameView.displayCurrentDay(currDay);

            // this loop iterates when a day ends
            while (activeScenes > 1) {
                PlayerController player = players.get(playerIndex);
                ActionType action;
                gameView.displayTurn(player.getPlayerName());
                boolean hasMoved = false;
                boolean hasTaken = false;
                boolean hasWorked = false;

                // controls the players inputs and makes sure they are taking a valid turn
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
                            gameView.displayCurrentLocation(players.get(playerIndex).getPlayerLocation().getLocationName());
                            break;
                        case WHO:
                            gameView.displayCurrentDetails(player.getPlayerName(), player.getPlayerRank(), player.getPlayerDollars(), player.getPlayerCredits(),
                            player.getPlayerRehearsals(), player.getPlayerLocation().getLocationName(), player.getPlayerRole());
                            break;
                        case DETAILS:
                            for (int j = 0; j < players.size(); j++) {
                                PlayerController p = players.get(j);
                                gameView.displayDetails(p.getPlayerName(), p.getPlayerRank(), p.getPlayerDollars(), p.getPlayerCredits(),
                                    p.getPlayerRehearsals(), p.getPlayerLocation().getLocationName(), p.getPlayerRole());
                            }
                            break;
                        case END:
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
        // display winners when the game is over
        ArrayList<String> winners = ScoreCalculator.getWinners(players);
        gameView.displayWinners(winners);
    }

    public static void decrementActiveScenes() {
        activeScenes--;
    }
}