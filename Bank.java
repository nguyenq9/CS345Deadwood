import java.util.ArrayList;
import java.util.Arrays;

public class Bank {
    private static int[] upgradeDollarCosts;
    private static int[] upgradeCreditCosts;

    public static void payActingRewards(PlayerController player, boolean onCard, boolean success) {
        if (onCard && success) {
            int totalC = player.getPlayerCredits() + 2;
            player.setPlayerCredits(totalC);
        } else if (!onCard) {
            if (success) {
                int totalC = player.getPlayerCredits() + 1;
                player.setPlayerCredits(totalC);
            }
            int totalD = player.getPlayerDollars() + 1;
            player.setPlayerDollars(totalD);
        } 
    }

    public static void payBonusRewards(Set set) {
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

        for (int i = budget; i < budget; i++) {
            totalBonuses[i % numRoles] += bonuses[i - 1];
        }

        for (int i = 0; i < numRoles; i++) {
            if (roles.get(i).getTaken()) {
                PlayerController currentPlayer = roles.get(i).getActor();
                int newDollars =
                    currentPlayer.getPlayerDollars() + totalBonuses[i];
                currentPlayer.setPlayerDollars(newDollars);
            }
        }

        for (int i = 0; i < set.getRoles().size(); i++) {
            if (set.getRoles().get(i).getTaken()) {
                PlayerController currentPlayer = roles.get(i).getActor();
                int newDollars = currentPlayer.getPlayerDollars() + 2;    
                currentPlayer.setPlayerDollars(newDollars);
            }
        }
    }

    public static int[] getUpgradeDollarCosts() {
        return upgradeDollarCosts;
    }

    public static int[] getUpgradeCreditCosts() {
        return upgradeCreditCosts;
    }

    public static int getDollarUpgradeCost(int desiredRank) {
        return upgradeDollarCosts[desiredRank - 2];
    }

    public static int getCreditUpgradeCost(int desiredRank) {
        return upgradeCreditCosts[desiredRank - 2];
    }

}