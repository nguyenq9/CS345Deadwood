import java.util.ArrayList;

public class PlayerController {
    private Player player;
    private GameView playerView;
    private BoardController boardController;

    public PlayerController(Player player, GameView playerView, BoardController boardController) {
        this.player = player;
        this.playerView = playerView;
        this.boardController = boardController;
    }

    /// returns true if the player successfully moves, or false if they don't
    public boolean move() {
        Location currLocation = getPlayerLocation();
        ArrayList<Location> adj = currLocation.getAdjacentLocations();
        ArrayList<String> locationStrings = new ArrayList<String>();
        for (int i = 0; i < adj.size(); i++) {
            locationStrings.add(adj.get(i).getLocationName());
        }
        playerView.displayMoveOption(locationStrings);

        String input = playerView.getMoveOption();
        for (int i = 0; i < adj.size(); i++) {
            if (adj.get(i).getLocationName().toLowerCase().equals(input)) {
                setPlayerLocation(adj.get(i));
                playerView.displayMovingOutcome(getPlayerName(), getPlayerLocation().getLocationName());
                return true;
            }
        }
        playerView.displayMovingFail();
        return false;
    }

    /// returns true if the player successfully takes a role, or false if they don't
    public boolean take() {
        ArrayList<Set> boardSets = boardController.getBoardSets();
        Set currSet = null;

        // Finds the current set user is one
        int setIndex = 0;
        for (int i = 0; i < boardSets.size(); i++) {
            if (boardSets.get(i).getLocationName().toLowerCase().equals(
                getPlayerLocation().getLocationName().toLowerCase())) {
                    currSet = boardSets.get(i);
                    setIndex = i;
            }
        }

        if (currSet == null || currSet.isWrapped()) {
            playerView.displayCantTakeRole();
            return false;
        }

        // Displays all available roles
        ArrayList<Role> setRoles = currSet.getRoles();  // off card
        ArrayList<String> roleStrings = new ArrayList<String>();
        ArrayList<Boolean> roleBoolean = new ArrayList<Boolean>();
        ArrayList<Integer> roleRank = new ArrayList<Integer>();
        for (int i = 0; i < setRoles.size(); i++) {
            if (!setRoles.get(i).getIsTaken()) {
                roleStrings.add(setRoles.get(i).getRoleName());
                roleBoolean.add(setRoles.get(i).getOnCard());
                roleRank.add(setRoles.get(i).getRank());
            }
        }
        ArrayList<Role> sceneRoles = currSet.getScene().getRoles(); // on card
        for (int i = 0; i < sceneRoles.size(); i++) {
            if (!sceneRoles.get(i).getIsTaken()) {
                roleStrings.add(sceneRoles.get(i).getRoleName());
                roleBoolean.add(sceneRoles.get(i).getOnCard());
                roleRank.add(sceneRoles.get(i).getRank());
            }
        }
        playerView.displayRoleOptions(roleStrings, roleRank, roleBoolean);

        // get user input
        String input = playerView.getTakeRoleOption();
        for (int i = 0; i < setRoles.size(); i++) {
            if (setRoles.get(i).getRoleName().toLowerCase().equals(input) && !setRoles.get(i).getIsTaken() && setRoles.get(i).getRank() <= getPlayerRank()) {
                setPlayerSet(currSet);
                setPlayerRole(setRoles.get(i));
                boardController.getBoardSets().get(setIndex).getRoles().get(i).setActor(this);
                setPlayerIsWorking(true);
                boardController.getBoardSets().get(setIndex).getRoles().get(i).setIsTaken(true);
                playerView.displayTakeRoleOutcome(getPlayerName(), setRoles.get(i).getRoleName());
                return true;
            }
        }
        for (int i = 0; i < sceneRoles.size(); i++) {
            if (sceneRoles.get(i).getRoleName().toLowerCase().equals(input) && !sceneRoles.get(i).getIsTaken() && sceneRoles.get(i).getRank() <= getPlayerRank()) {
                setPlayerSet(currSet);
                setPlayerRole(sceneRoles.get(i));
                boardController.getBoardSets().get(setIndex).getRoles().get(i).setActor(this);
                setPlayerIsWorking(true);
                boardController.getBoardSets().get(setIndex).getScene().getRoles().get(i).setIsTaken(true);
                playerView.displayTakeRoleOutcome(getPlayerName(), sceneRoles.get(i).getRoleName());
                return true;
            }
        }
        playerView.displayTakingRoleFail();
        return false;

    }

    // returns true if the player successfully upgrades, or false if they don't
    public boolean upgrade() {
        if (!getPlayerLocation().getLocationName().toLowerCase().equals("office")){
            playerView.displayNotAtOffice();
            return false;
        }
        playerView.displayUpgradeOptions(this, boardController);
        int choiceRank = playerView.getUpgradeOption();
        if (choiceRank <= 6 && choiceRank > this.getPlayerRank()) {
            String currency = playerView.getUpgradeCurrency();
            if (currency.toLowerCase().equals("credits")) {
                int creditCost = boardController.getBoardOffice().getCreditUpgradeCost(choiceRank);
                if (getPlayerCredits() >= creditCost) {
                    setPlayerRank(choiceRank);
                    setPlayerCredits(getPlayerCredits() - creditCost);
                    playerView.displayUpgradeSuccess(choiceRank);
                    return true;
                }
            } else if (currency.toLowerCase().equals("dollars")) {
                int dollarCost = boardController.getBoardOffice().getDollarUpgradeCost(choiceRank);
                if (getPlayerDollars() >= dollarCost) {
                    setPlayerRank(choiceRank);
                    setPlayerDollars(getPlayerDollars() - dollarCost); 
                    playerView.displayUpgradeSuccess(choiceRank);
                    return true;
                }
            }
        }
        playerView.displayUpgradeFail();
        return false;
    }

    // returns true if th eplayer successfully rehearses, or false if they don't
    public boolean rehearse() {
        Set set = getPlayerSet();
        if (getPlayerRole() == null || set == null || set.isWrapped()) {
            return false;
        }
        if (getPlayerRehearsals() == set.getScene().getBudget()) {
            return act();
        } else {
            incrementPlayerRehearsals();
            playerView.displayRehearsingOutcome(getPlayerRole().getRoleLine(), getPlayerName());
            return true;
        }
    }

    // returns true if the player successfully acts (regardless of if the acting suceeds or fails), or false if they fail to act
    public boolean act() {
        Set set = getPlayerSet();
        int roll = Dice.roll() + getPlayerRehearsals();
        int budget = set.getScene().getBudget();
        boolean success = roll >= budget;
        Bank.payActingRewards(this, getPlayerRole().getOnCard(), success);
        playerView.displayActRoll(roll);
        playerView.displayActingOutcome(getPlayerRole().getRoleLine(), success);
        if (success) {
            set.decrementShotCounters();
            if (set.getShotCounters() == 0) {
                playerView.displaySceneWrapped();
                set.wrapScene();
            }
        }
        return true;
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