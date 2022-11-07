import java.util.ArrayList;

public class CastingOffice implements Location {
    private ArrayList<Location> adjLocation;

    public CastingOffice(ArrayList<Location> adjLocation) {  
        this.adjLocation = adjLocation;
    }
    
    public ArrayList<Location> getAdjacentLocations() {
        return adjLocation;
    }

    public String getLocationType() {
        return "CastingOffice";
    }
}