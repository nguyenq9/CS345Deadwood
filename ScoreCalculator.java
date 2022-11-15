import java.util.ArrayList;

public class ScoreCalculator {

    // returns list of players in case of a tie
    public static ArrayList<PlayerController> getWinners(ArrayList<PlayerController> players) {
        int maxScore = 0;
        for (int i = 0; i < players.size(); i++) {
            int playerScore = getScore(players.get(i));
            if (playerScore >= maxScore) {
                maxScore = playerScore;
            }
        }

        ArrayList<PlayerController> winners = new ArrayList<PlayerController>();
        for (int i = 0; i < players.size(); i++) {
            int playerScore = getScore(players.get(i));
            if (playerScore == maxScore) {
                winners.add(players.get(i));
            }
        }
        return winners;
    }

    // calculates score for given player
    private static int getScore(PlayerController player) {
        int dollars = player.getPlayerDollars();
        int credits = player.getPlayerCredits();
        int rank = player.getPlayerRank();
        int playerScore = dollars + credits + 5 * rank;
        return playerScore;
    }
}
