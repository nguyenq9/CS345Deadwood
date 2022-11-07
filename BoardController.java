public class BoardController {
    Board board;
    BoardView boardView;

    public BoardController(Board board, BoardView boardView) {
        this.board = board;
        this.boardView = boardView;
    }

    public void updateBoardView() {
        // Make a call to boardView to display the board
    }
}