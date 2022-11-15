import java.util.ArrayList;

public class BoardController {
    private Board board;
    private GameView gameView;

    public BoardController(Board board, GameView gameView) {
        this.board = board;
        this.gameView = gameView;
    }

    public ArrayList<Location> getBoardLocations() {
        return board.getLocations();
    }

    public ArrayList<Set> getBoardSets() {
        return board.getSets();
    }

    public Trailer getBoardTrailer() {
        return board.getTrailer();
    }

    public CastingOffice getBoardOffice() {
        return board.getOffice();
    }

    public void updateBoardView() {
        // Will be used once we have a GUI
    }
}