import java.util.ArrayList;
import java.util.Arrays;

public class Bank {

    private static GameView gameView = GameView.gameView;

    public static void payActingRewards(PlayerController player, boolean onCard, boolean success) {
        if (onCard && success) {
            player.addPlayerCredits(2);
            gameView.displayCreditEarnings(player, 2);
        } else if (!onCard) {
            if (success) {
                player.addPlayerCredits(1);
                gameView.displayCreditEarnings(player, 1);
            }
            player.addPlayerDollars(1);
            gameView.displayDollarEarnings(player, 1);
        } 
    }

    public static void payBonusRewards(Set set) {
        gameView.displayBonus();
        Scene scene = set.getScene();
        int budget = scene.getBudget();

        ArrayList<Role> roles = scene.getRoles();
        int numRoles = roles.size();

        int[] bonuses = new int[budget];
        for (int i = 0; i < budget; i++) {
            bonuses[i] = Dice.roll();
        }

        Arrays.sort(bonuses);
        int[] totalBonuses = new int[numRoles];

        for (int i = 0; i < budget; i++) {
            totalBonuses[(numRoles - 1) - (i % numRoles)] += bonuses[budget - 1 - i];
        }

        for (int i = 0; i < numRoles; i++) {
            if (roles.get(i).getIsTaken()) {
                roles.get(i).getActor().addPlayerDollars(totalBonuses[i]);
                gameView.displayDollarEarnings(roles.get(i).getActor(), totalBonuses[i]);
            }
        }

        for (int i = 0; i < set.getRoles().size(); i++) {
            if (set.getRoles().get(i).getIsTaken()) {
                int rank = set.getRoles().get(i).getRank();
                set.getRoles().get(i).getActor().addPlayerDollars(rank);
                gameView.displayDollarEarnings(set.getRoles().get(i).getActor(), rank);
            }
        }
    }

}
