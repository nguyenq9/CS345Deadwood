import java.util.ArrayList;

// Maybe make this class static so we can access the adjLocation attribute in player.

public class Trailer implements Location{
    private ArrayList<Location> adjLocations;

    public Trailer(ArrayList<Location> adjLocations) {
        this.adjLocations = adjLocations;
    }
    
    public ArrayList<Location> getAdjacentLocations() {
        return adjLocations;
    }

    public String getLocationType() {
        return "Trailer";
    }
}
