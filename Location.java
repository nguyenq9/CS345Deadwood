import java.util.ArrayList;

public interface Location {
    public String getLocationName();
    public ArrayList<Location> getAdjacentLocations();
    public ArrayList<PlayerController> getPlayers();
    public void addPlayer(PlayerController player);
    public void removePlayer(PlayerController player);
    public void clearPlayers();
    public void setAdjacentLocations(ArrayList<Location> locations);
    public String getLocationType();
    public int[] getLocationArea();
}