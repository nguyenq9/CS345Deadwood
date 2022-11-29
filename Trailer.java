import java.util.ArrayList;

public class Trailer implements Location{
    private ArrayList<Location> adjLocations;
    private int[] locationArea;
    private ArrayList<PlayerController> players;

    public Trailer(ArrayList<Location> adjLocations, int[] locationArea) {
        this.adjLocations = adjLocations;
        this.locationArea = locationArea;
        players = new ArrayList<PlayerController>();
    }

    public String getLocationName() {
        return "trailer";
    }
    
    public ArrayList<Location> getAdjacentLocations() {
        return adjLocations;
    }

    public void setAdjacentLocations(ArrayList<Location> locations) {
        adjLocations = locations;
    }

    public String getLocationType() {
        return "Trailer";
    }

    public int[] getLocationArea() {
        return locationArea;
    }

    public void resetPlayerLocations(ArrayList<PlayerController> players) {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setPlayerLocation(this);
        }
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
}
