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

    public ArrayList<String> getAdjacentLocationNames() {
        ArrayList<Location> locations = getPlayerLocation().getAdjacentLocations();
        ArrayList<String> locationNames = new ArrayList<String>();
        for (int i = 0; i < locations.size(); i++) {
            locationNames.add(locations.get(i).getLocationName());
        }
        return locationNames;
    }

    // returns true if the player successfully moves, or false if they don't
    public void move(Location location) {
        GUIView.clearHightlightLocations(getAdjacentLocationNames());
        Location prevLocation = getPlayerLocation();
        setPlayerLocation(location);
        GUIView.updatePlayerLocation(prevLocation);
        GUIView.updatePlayerLocation(location);
    }

    public void take(Role role) {
        ArrayList<Role> roles = getAvailableRoles();
        if (roles.contains(role)) {
            setPlayerRole(role);
            setPlayerIsWorking(true);
            GUIView.movePlayerToRole(getPlayerName(), role.getRoleName(), role.getRank(), getPlayerLocation().getLocationName(), role.getOnCard());
            ArrayList<String> roleNames = new ArrayList<String>();
            ArrayList<Integer> roleRanks = new ArrayList<Integer>();
            for (int i = 0; i < roles.size(); i++) {
                roleNames.add(roles.get(i).getRoleName());
                roleRanks.add(roles.get(i).getRank());
            }
            GUIView.clearHighlightRoles(getPlayerLocation().getLocationName(), roleNames, roleRanks);
        }
        role.setIsTaken(true);
    }

    // returns true if the player successfully acts (regardless of if the acting suceeds or fails), or false if they fail to act
    public boolean act() {
        Location location = getPlayerLocation();
        Set currSet = getSet(location);

        if (currSet == null)  {
            return false;
        }

        int roll = Dice.roll() + getPlayerRehearsals();
        int budget = currSet.getScene().getBudget();
        boolean success = roll >= budget;

        // TEMPORARY
        success = true;

        GUIView.displayActInformation(roll, success);
        playerView.displayActRoll(roll);
        playerView.displayActingOutcome(getPlayerRole().getRoleLine(), success);

        bank.payActingRewards(this, getPlayerRole().getOnCard(), success);
        updatePlayerGUI();

        if (success) {
            GUIView.removeShotCounter(currSet);
            currSet.decrementShotCounters();
            if (currSet.getShotCounters() == 0) {
                playerView.displaySceneWrapped();

                ArrayList<PlayerController> players = currSet.getPlayers();
                for (PlayerController i : players) {
                    i.setPlayerIsWorking(false);
                    i.setPlayerSet(null);
                }
                
                currSet.wrapScene();
                GUIView.removeScene(currSet);
                GUIView.updatePlayerLocation(location);
            }
        }
        return true;
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

    public void upgrade() {
        GUIView.highlightUpgrades(boardController.getBoardOffice().getAvailableUpgrades(getPlayerRank(),
        getPlayerDollars(), getPlayerCredits()));
    }

    // returns true if the player successfully upgrades, or false if they don't
    public void upgradeChoice(int choiceRank, Currency currency) {
        CastingOffice office = boardController.getBoardOffice();
        if (choiceRank <= 6 && choiceRank > this.getPlayerRank()) {
            if (currency == Currency.CREDITS) {
                int creditCost = office.getCreditUpgradeCost(choiceRank);
                if (getPlayerCredits() >= creditCost) {
                    setPlayerRank(choiceRank);
                    setPlayerCredits(getPlayerCredits() - creditCost);
                    playerView.displayUpgradeOutcome(choiceRank);
                    GUIView.updatePlayerRank(getPlayerName(), choiceRank);
                }
            } else if (currency == Currency.DOLLARS) {
                int dollarCost = office.getDollarUpgradeCost(choiceRank);
                if (getPlayerDollars() >= dollarCost) {
                    setPlayerRank(choiceRank);
                    setPlayerDollars(getPlayerDollars() - dollarCost); 
                    playerView.displayUpgradeOutcome(choiceRank);
                    GUIView.updatePlayerRank(getPlayerName(), choiceRank);
                }
            } 
        }

        GUIView.clearHighlightUpgrades();
        updatePlayerGUI();
    }

    public void clearHighlighting() {
        GUIView.clearHightlightLocations(getAdjacentLocationNames());
        ArrayList<Role> roles = getAvailableRoles();
        ArrayList<String> roleNames = new ArrayList<String>();
        ArrayList<Integer> roleRanks = new ArrayList<Integer>();
        for (int i = 0; i < roles.size(); i++) {
            roleNames.add(roles.get(i).getRoleName());
            roleRanks.add(roles.get(i).getRank());
        }
        GUIView.clearHighlightRoles(getPlayerLocation().getLocationName(), roleNames, roleRanks);
        GUIView.clearActInformation();
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

    public void updatePlayerGUI() {
        GUIView.displayPlayerInfo(getPlayerName(), getPlayerRank(),
            getPlayerDollars(), getPlayerCredits(), getPlayerRehearsals());
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