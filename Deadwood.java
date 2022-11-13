import java.util.ArrayList;

public class Deadwood {  
    private static ArrayList<PlayerController> players;
    private static BoardController board;
    private static int maxDays;
    private static ArrayList<Scene> cardScenes;
    private static int activeScenes;
    private static GameView gameView = GameView.gameView;

    public static void main(String[] args) {
        // Make calls to Setup to initialize game components
        // players = SetUp.initializePlayers(8);
        // board = SetUp.initializeBoard();
        // maxDays = SetUp.getMaxDays(8);

        // Big loop to control turns
        board = SetUp.initializeBoard();
        cardScenes = SetUp.initializeCards();

        int playerCount = 0;
        if (args.length > 0) {
            playerCount = Integer.parseInt(args[0]);
        } else {
            playerCount = gameView.getPlayerCount();
        }
        
        maxDays = SetUp.getMaxDays(playerCount);
        ArrayList<String> playerNames = gameView.getPlayerNames(playerCount);

        players = SetUp.initializePlayers(playerCount, playerNames, board.getBoardTrailer());

        for (int i = 0; i < maxDays; i++) {
            activeScenes = board.getBoardSets().size();
            // game loop
            // action keywords: move, take, act, rehearse, upgrade, end
            // info keywords: where, who, where all, help

            int playerIndex = 0;
            while (activeScenes > 1) {
                PlayerController player = players.get(playerIndex);
                ActionType action;
                System.out.println(player.getPlayerName() + "! It's your turn!");
                boolean hasMoved = false;
                boolean hasTaken = false;
                boolean hasWorked = false;
                do {
                    action = GameView.getPlayerInput();
                    switch (action) {
                        case HELP:
                            gameView.displayHelp();
                            break;
                        case MOVE:
                            if (!hasMoved) {
                                player.move();
                                hasMoved = true;
                            }
                            break;
                        case TAKE:
                            if (!hasTaken) {
                                // ADD CODE
                                hasTaken = true;
                            }
                            break;
                        case ACT:
                            if (!hasMoved && !hasWorked) {
                                player.act();
                                hasWorked = true;
                            }
                            break;
                        case REHEARSE:
                            if (!hasWorked) {
                                player.rehearse();
                                hasWorked = true;
                            }
                            break;
                        case UPGRADE:
                            player.upgrade();
                            break;
                        case END:
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
                    }
                } while (action != ActionType.END);

                // next player turn
                playerIndex++;
                if (playerIndex >= playerCount) {
                    playerIndex = 0;
                }
            }
        }
    }
}