import java.util.ArrayList;
import java.util.Collections;

public class SetUp {

    public static BoardController initializeBoard() {
        try {
            Board board = XMLParser.parseBoard();
            BoardController boardController = new BoardController(board, GameView.gameView);
            return boardController;
        } catch (Exception e) {
            System.out.println("ERROR: Failed to parse board");
            // Add code for if the XML Parser throws an error
            return null;
        }
    }

    public static ArrayList<Scene> initializeCards() {
        try {
            ArrayList<Scene> cardScenes = XMLParser.parseCards();
            Collections.shuffle(cardScenes);
            return cardScenes;
        } catch (Exception e) {
            System.out.println("ERROR: Failed to parse scenes");
            // Add code for if the XML Parser throws an error
            return null;
        }
    }

    public static void assignScenes(BoardController board, ArrayList<Scene> scenes) {
        ArrayList<Set> sets = board.getBoardSets();
        for (int i = 0; i < sets.size(); i++) {
            sets.get(i).setScene(scenes.remove(0));
        }
    }

    public static ArrayList<PlayerController> initializePlayers(int numPlayers, ArrayList<String> playerNames, Trailer trailer) {
        ArrayList<PlayerController> players = new ArrayList<PlayerController>();
        for (int i = 0; i < numPlayers; i++) {
            Player newPlayer = new Player(playerNames.get(i), trailer);
            PlayerController newPlayerController = new PlayerController(newPlayer, GameView.gameView);
            players.add(newPlayerController);
        }
        if (numPlayers == 7 || numPlayers == 8) {
            assignStartingRank(players);
        }
        if (numPlayers == 5 || numPlayers == 6) {
            assignStartingCredits(players);
        }
        assignTurnOrder(players);
        return players;
    }

    private static ArrayList<PlayerController> assignTurnOrder(ArrayList<PlayerController> players) {
        Collections.shuffle(players);
        return players;
    }

    private static void assignStartingRank(ArrayList<PlayerController> players) {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setPlayerRank(2);
        }
    }

    private static void assignStartingCredits(ArrayList<PlayerController> players) {
        int numPlayers = players.size();
        for (int i = 0; i < numPlayers; i++) {
            if (numPlayers == 5) {
                players.get(i).setPlayerCredits(2);
            } else {
                players.get(i).setPlayerCredits(4);
            }
        }
    }

    public static int getMaxDays(int numPlayers) {
        if (numPlayers == 2 || numPlayers == 3) {
            return 3;
        } else {
            return 4;
        }
    }

}
