import java.util.ArrayList;

public interface Location {
    public ArrayList<Location> getAdjacentLocations();
    public String getLocationType();
}