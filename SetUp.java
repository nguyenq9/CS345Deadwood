import java.util.ArrayList;

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

    public static ArrayList<PlayerController> initializePlayers(int players) {
        // Make instances of players
        // Figure out the turn order
        // Assign starting credits
        // Assign starting rank
        // Return initialized list of players
        return null;
    }

    private ArrayList<PlayerController> assignTurnOrder(ArrayList<PlayerController> players) {
        return null;
    }

    private void assignStartingRank(ArrayList<PlayerController> players) {
    
    }

    private void assignStartingCredits(ArrayList<PlayerController> players) {
        
    }

    public static int getMaxDays(int playerCount) {
        return 0;
    }

}
