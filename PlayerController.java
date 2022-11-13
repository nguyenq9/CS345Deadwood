import java.util.ArrayList;

public class PlayerController {
    private Player player;
    private GameView playerView;

    public PlayerController(Player player, GameView playerView) {
        this.player = player;
        this.playerView = playerView;
    }

    public void move() {
        if (getPlayerIsWorking()) {
            return;
        }
        Location currLocation = getPlayerLocation();
        ArrayList<Location> adj = currLocation.getAdjacentLocations();
        for (int i = 0; i < adj.size(); i++) {
            System.out.println("[" + i + "] " + adj.get(i).getLocationName());
        }
        String input = playerView.getMoveOption();
        for (int i = 0; i < adj.size(); i++) {
            if (adj.get(i).getLocationName().toLowerCase().equals(input)) {
                setPlayerLocation(adj.get(i));
            }
        }
    }

    public void upgrade() {
        
    }

    public void rehearse() {
        Set set = player.getSet();
        if (player.getRole() == null ||
            set == null ||
            set.isWrapped()) {
            return;
        }
        if (getPlayerRehearsals() == set.getScene().getBudget()) {
            act();
        } else {
            incrementPlayerRehearsals();
        }
    }

    public void act() {
        Set set = player.getSet();
        if (!player.getIsWorking() || set.isWrapped()) {
            playerView.displayActingFail();
            return;
        }
        int roll = Dice.roll();
        int budget = set.getScene().getBudget();
        boolean success = roll + getPlayerRehearsals() >= budget;
        if (success) {
            set.decrementShotCounters();
            if (set.getShotCounters() == 0) {
                Bank.payBonusRewards(set);
            }
        }
        Bank.payActingRewards(this, getPlayerRole().getOnCard(), success);
        playerView.displayActingSuccess(success);
    }

    public String getPlayerName() {
        return player.getName();
    }

    public int getPlayerDollars() {
        return player.getDollars();
    }

    public int getPlayerCredits() {
        return player.getCredits();
    }

    public int getPlayerRank() {
        return player.getRank();
    }

    public int getPlayerRehearsals() {
        return player.getRehearsals();
    }

    public boolean getPlayerIsWorking() {
        return player.getIsWorking();
    }

    public Set getPlayerSet() {
        return player.getSet();
    }

    public Role getPlayerRole() {
        return player.getRole();
    }

    public Location getPlayerLocation() {
        return player.getLocation();
    }

    public void setPlayerName(String newName) {
        player.setName(newName);
    }

    public void setPlayerDollars(int newDollars) {
        player.setDollars(newDollars);
    }

    public void setPlayerCredits(int newCredits) {
        player.setCredits(newCredits);
    }

    public void setPlayerRank(int newRank) {
        player.setRank(newRank);
    }

    public void setPlayerRehearsals(int rehearsals) {
        player.setRehearsals(rehearsals);
    }

    public void incrementPlayerRehearsals() {
        player.incrementRehearsals();
    }

    public void setPlayerIsWorking(boolean isWorking) {
        player.setIsWorking(isWorking);
    }

    public void setPlayerSet(Set set) {
        player.setSet(set);
    }

    public void setPlayerRole(Role newRole) {
        player.setRole(newRole);
    }

    public void setPlayerLocation(Location newLocation) {
        player.setLocation(newLocation);
    }

}