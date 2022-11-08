import java.util.ArrayList;
import java.util.Collections;

public class SetUp {

    public static BoardController initializeBoard() {
        try {
            ArrayList<Location> locations = XMLParser.parseBoard();
            Board board = new Board(locations);
            BoardView boardView = new BoardView();
            BoardController boardController = new BoardController(board, boardView);
            return boardController;
        } catch (Exception e) {
            // Add code for if the XML Parser throws an error
            return null;
        }
    }

    public static ArrayList<PlayerController> initializePlayers(int numPlayers) {
        ArrayList<PlayerController> players = new ArrayList<PlayerController>();
        for (int i = 1; i <= numPlayers; i++) {
            Player newPlayer = new Player("Player" + i);
            PlayerView newPlayerView = new PlayerView();
            PlayerController newPlayerController = new PlayerController(newPlayer, newPlayerView);
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
