import java.util.ArrayList;
import java.util.Collections;

public class SetUp {

    private static XMLParser xmlParser = XMLParser.xmlParser;

    public static SetUp setup = new SetUp();

    private SetUp() {

    }

    // constructs the board out of locations
    public BoardController initializeBoard() {
        try {
            Board board = xmlParser.parseBoard();
            BoardController boardController = new BoardController(board, GameView.gameView);
            return boardController;
        } catch (Exception e) {
            System.out.println("ERROR: Failed to parse board");
            e.printStackTrace();
            return null;
        }
    }

    // initializes the deck of scene cards and shuffles them
    public ArrayList<Scene> initializeCards() {
        try {
            ArrayList<Scene> cardScenes = xmlParser.parseCards();
            Collections.shuffle(cardScenes);
            return cardScenes;
        } catch (Exception e) {
            System.out.println("ERROR: Failed to parse scenes");
            e.printStackTrace();
            return null;
        }
    }

    // gives a new scene to each set
    public void assignScenes(BoardController board, ArrayList<Scene> scenes) {
        ArrayList<Set> sets = board.getBoardSets();
        for (int i = 0; i < sets.size(); i++) {
            sets.get(i).setScene(scenes.remove(0));
        }
    }

    // sets up starting player stats based on number of players
    public ArrayList<PlayerController> initializePlayers(int numPlayers, ArrayList<String> playerNames, BoardController board) {
        ArrayList<PlayerController> players = new ArrayList<PlayerController>();
        for (int i = 0; i < numPlayers; i++) {
            Player newPlayer = new Player(playerNames.get(i), board.getBoardTrailer());
            PlayerController newPlayerController = new PlayerController(newPlayer, GameView.gameView, board);
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

    // randomizes the player turn order
    private ArrayList<PlayerController> assignTurnOrder(ArrayList<PlayerController> players) {
        Collections.shuffle(players);
        return players;
    }

    // only called if 7 or 8 players
    private void assignStartingRank(ArrayList<PlayerController> players) {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setPlayerRank(2);
        }
    }

    // only called if 5 or 6 players
    private void assignStartingCredits(ArrayList<PlayerController> players) {
        int numPlayers = players.size();
        for (int i = 0; i < numPlayers; i++) {
            if (numPlayers == 5) {
                players.get(i).setPlayerCredits(2);
            } else {
                players.get(i).setPlayerCredits(4);
            }
        }
    }

    public int getMaxDays(int numPlayers) {
        if (numPlayers == 2 || numPlayers == 3) {
            return 3;
        } else {
            return 4;
        }
    }
}
