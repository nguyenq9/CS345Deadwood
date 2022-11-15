import java.util.ArrayList;

// Maybe make this class static so we can access the adjLocation attribute in player.

public class Trailer implements Location{
    private ArrayList<Location> adjLocations;
    private int[] locationArea;

    public Trailer(ArrayList<Location> adjLocations, int[] locationArea) {
        this.adjLocations = adjLocations;
        this.locationArea = locationArea;
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
}
