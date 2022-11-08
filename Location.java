import java.util.ArrayList;

public interface Location {
    public String getLocationName();
    public ArrayList<Location> getAdjacentLocations();
    public void setAdjacentLocations(ArrayList<Location> locations);
    public String getLocationType();
    public int[] getLocationArea();
}