import java.util.ArrayList;

public class CastingOffice implements Location {
    private ArrayList<Location> adjLocations;
    private int[] locationArea;
    private ArrayList<PlayerController> players;
    private int[][] upgradeSelectionAreaCredits;
    private int[][] upgradeSelectionAreaDollars;
    private int[] upgradeDollarCosts;
    private int[] upgradeCreditCosts;

    

    public CastingOffice(ArrayList<Location> adjLocations, int[] locationArea, int[] upgradeDollarCosts, int[] upgradeCreditCosts, int[][] upgradeSelectionAreaCredits, int[][] upgradeSelectionAreaDollars) {  
        this.adjLocations = adjLocations;
        this.locationArea = locationArea;
        this.upgradeDollarCosts = upgradeDollarCosts;
        this.upgradeCreditCosts = upgradeCreditCosts;
        this.players = new ArrayList<PlayerController>();
        this.upgradeSelectionAreaCredits = upgradeSelectionAreaCredits;
        this.upgradeSelectionAreaDollars = upgradeSelectionAreaDollars;
    }

    public ArrayList<String> getAvailableUpgrades(int rank, int dollars, int credits) {
        ArrayList<String> upgrades = new ArrayList<String>();
        for (int i = rank - 1; i < 5; i++) {
            if (dollars >= upgradeDollarCosts[i]) {
                upgrades.add("d" + (i + 2));
            }
        }
        for (int i = rank - 1; i < 5; i++) {
            if (credits >= upgradeCreditCosts[i]) {
                upgrades.add("c" + (i + 2));
            }
        }
        return upgrades;
    }

    public String getLocationName() {
        return "office";
    }
    
    public ArrayList<Location> getAdjacentLocations() {
        return adjLocations;
    }

    public void setAdjacentLocations(ArrayList<Location> locations) {
        adjLocations = locations;
    }

    public String getLocationType() {
        return "CastingOffice";
    }

    public int[] getLocationArea() {
        return locationArea;
    }

    public int[] getUpgradeDollarCosts() {
        return upgradeDollarCosts;
    }

    public int[] getUpgradeCreditCosts() {
        return upgradeCreditCosts;
    }

    public int getDollarUpgradeCost(int desiredRank) {
        return upgradeDollarCosts[desiredRank - 2];
    }

    public int getCreditUpgradeCost(int desiredRank) {
        return upgradeCreditCosts[desiredRank - 2];
    }

    public ArrayList<PlayerController> getPlayers() {
		return players;
	}

	public void addPlayer(PlayerController player) {
		players.add(player);
	}

	public void removePlayer(PlayerController player) {
		players.remove(player);
	}

	public void clearPlayers() {
		players.clear();
	}

    public int[][] getUpgradeAreaCredit() {
        return this.upgradeSelectionAreaCredits;
    }

    public int[][] getUpgradeAreaDollar() {
        return this.upgradeSelectionAreaDollars;
    }
}