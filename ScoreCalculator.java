import java.util.ArrayList;

public class ScoreCalculator {
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

    public static int getScore(PlayerController player) {
        int dollars = player.getPlayerDollars();
        int credits = player.getPlayerCredits();
        int rank = player.getPlayerRank();
        int playerScore = dollars + credits + 5 * rank;
        return playerScore;
    }
}
