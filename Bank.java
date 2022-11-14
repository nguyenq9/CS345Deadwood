import java.util.ArrayList;
import java.util.Arrays;

public class Bank {

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
            if (roles.get(i).getIsTaken()) {
                int newDollars = roles.get(i).getActor().getPlayerDollars() + totalBonuses[i];
                roles.get(i).getActor().setPlayerDollars(newDollars);
            }
        }

        for (int i = 0; i < set.getRoles().size(); i++) {
            if (set.getRoles().get(i).getIsTaken()) {
                int newDollars = set.getRoles().get(i).getActor().getPlayerDollars() + set.getRoles().get(i).getRank();
                set.getRoles().get(i).getActor().setPlayerDollars(newDollars);
            }
        }
    }

}
