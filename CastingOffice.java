import java.util.ArrayList;

public class CastingOffice implements Location {
    private ArrayList<Location> adjLocations;
    private int[] locationArea;

    public CastingOffice(ArrayList<Location> adjLocations, int[] locationArea) {  
        this.adjLocations = adjLocations;
        this.locationArea = locationArea;
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
}