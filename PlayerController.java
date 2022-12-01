import java.util.ArrayList;

public class PlayerController {
    private Player player;
    private GameView playerView;
    private BoardController boardController;
    private Bank bank = Bank.bank;

    public PlayerController(Player player, GameView playerView, BoardController boardController) {
        this.player = player;
        this.playerView = playerView;
        this.boardController = boardController;
    }

    // returns true if the player successfully moves, or false if they don't
    public void move() {
        ArrayList<Role> roles = getAvailableRoles();
        GUIView.clearHighlightRoles(roles);
        GUIView.highlightLocations(getPlayerLocation().getAdjacentLocations());
    }

    public void take() {
        GUIView.clearHightlightLocations(getPlayerLocation().getAdjacentLocations());
        ArrayList<Role> roles = getAvailableRoles();
        if (!roles.isEmpty()) {
            GUIView.highlightRoles(roles);
        } else {
            System.out.println("Available roles is null");
        }
    }

    public void role(Role role) {
        ArrayList<Role> roles = getAvailableRoles();
        if (roles.contains(role)) {
            setPlayerRole(role);
            setPlayerIsWorking(true);
            GUIView.movePlayerToRole(getPlayerName(), role.getRoleName(), role.getOnCard());
            GUIView.clearHighlightRoles(roles);
        }
        role.setIsTaken(true);
    }

    // returns true if the player successfully upgrades, or false if they don't
    public boolean upgrade() {
        CastingOffice office = boardController.getBoardOffice();
        playerView.displayUpgradeOptions(getPlayerRank(), office.getUpgradeDollarCosts(), office.getUpgradeCreditCosts());
        int choiceRank = playerView.getUpgradeOption();
        if (choiceRank <= 6 && choiceRank > this.getPlayerRank()) {
            Currency currency = playerView.getUpgradeCurrency();
            if (currency == Currency.CREDITS) {
                int creditCost = office.getCreditUpgradeCost(choiceRank);
                if (getPlayerCredits() >= creditCost) {
                    setPlayerRank(choiceRank);
                    setPlayerCredits(getPlayerCredits() - creditCost);
                    playerView.displayUpgradeOutcome(choiceRank);
                    return true;
                }
            } else if (currency == Currency.DOLLARS) {
                int dollarCost = office.getDollarUpgradeCost(choiceRank);
                if (getPlayerDollars() >= dollarCost) {
                    setPlayerRank(choiceRank);
                    setPlayerDollars(getPlayerDollars() - dollarCost); 
                    playerView.displayUpgradeOutcome(choiceRank);
                    return true;
                }
            } 
        }
        playerView.displayErrorMessage(ErrorType.INVALID_RANK);
        return false;
    }

    // returns true if the eplayer successfully rehearses, or false if they don't
    public boolean rehearse() {
        Location location = getPlayerLocation();
        Set currSet = getSet(location);

        if (currSet == null)  {
            return false;
        }

        if (getPlayerRehearsals() == currSet.getScene().getBudget()) {
            return act();
        } else {
            incrementPlayerRehearsals();
            GUIView.displayPlayerInfo(getPlayerName(), getPlayerRank(),
                getPlayerDollars(), getPlayerCredits(), getPlayerRehearsals());
            return true;
        }
    }

    // returns true if the player successfully acts (regardless of if the acting suceeds or fails), or false if they fail to act
    public boolean act() {
        Set set = getPlayerSet();
        int roll = Dice.roll() + getPlayerRehearsals();
        int budget = set.getScene().getBudget();
        boolean success = roll >= budget;

        GUIView.displayActInformation(roll, success);
        playerView.displayActRoll(roll);
        playerView.displayActingOutcome(getPlayerRole().getRoleLine(), success);

        bank.payActingRewards(this, getPlayerRole().getOnCard(), success);
        GUIView.displayPlayerInfo(getPlayerName(), getPlayerRank(),
            getPlayerDollars(), getPlayerCredits(), getPlayerRehearsals());
        
        if (success) {
            set.decrementShotCounters();
            if (set.getShotCounters() == 0) {
                playerView.displaySceneWrapped();
                set.wrapScene();
                GUIView.wrapScene();
            }
        }

        return true;
    }

    public ArrayList<Role> getAvailableRoles() {
        ArrayList<Role> availableRoles = new ArrayList<Role>();
        Location location = getPlayerLocation();
        Set currSet = getSet(location);

        if (currSet == null) {
            return availableRoles;
        }
        
        // Set roles
        for (int i = 0; i < currSet.getRoles().size(); i++) {
            if (currSet.getRoles().get(i).getRank() <= getPlayerRank() &&
                                        !currSet.getRoles().get(i).getIsTaken()) {
                availableRoles.add(currSet.getRoles().get(i));
            }
        }

        // Set scene roles
        Scene setScene = currSet.getScene();
        for (int i = 0; i < setScene.getRoles().size(); i++) {
            if ( setScene.getRoles().get(i).getRank() <= getPlayerRank() &&
                                        !setScene.getRoles().get(i).getIsTaken()) {
                availableRoles.add(setScene.getRoles().get(i));
            }
        }

        return availableRoles;
    }

    public Set getSet(Location location) {
        ArrayList<Set> boardSets = boardController.getBoardSets();
        Set currSet = null;
        for (int i = 0; i < boardSets.size(); i++) {
            if (boardSets.get(i).getLocationName().toLowerCase().equals(
                getPlayerLocation().getLocationName().toLowerCase())) {
                    currSet = boardSets.get(i);
            }
        }

        return currSet;
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

    public void addPlayerDollars(int amount) {
        player.addDollars(amount);
    }

    public void deductPlayerDollars(int amount) {
        player.deductDollars(amount);
    }

    public void setPlayerCredits(int newCredits) {
        player.setCredits(newCredits);
    }

    public void addPlayerCredits(int amount) {
        player.addCredits(amount);
    }

    public void deductPlayerCredits(int amount) {
        player.deductCredits(amount);
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
        getPlayerLocation().removePlayer(this);
        player.setLocation(newLocation);
        newLocation.addPlayer(this);
    }

}