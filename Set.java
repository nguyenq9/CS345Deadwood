import java.util.ArrayList;

public class Set implements Location {
    private String setName; 
    private Scene scene;
    private ArrayList<Role> roles;
    private int maxShotCounter;
    private int shotCounters;
    private boolean wrapped;
    private ArrayList<Location> adjLocations;   // Added adjLocation attribute

    public Set(String setName, ArrayList<Role> roles, int maxShotCounter, ArrayList<Location> adjLocations) {
        this.setName = setName;
        this.scene = null;
        this.roles = roles;
        this.maxShotCounter = maxShotCounter;
        this.shotCounters = 0;
        this.adjLocations = adjLocations;
    }

    public ArrayList<Location> getAdjacentLocations() {
        return adjLocations;
    }

    public String getLocationType() {
        return "Set";
    }

    public Scene getScene() {
        return scene;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public boolean isWrapped() {
        return wrapped;
    }

    public int getMaxShotCounter() {
        return maxShotCounter;
    }

    public int getShotCounters() {
        return shotCounters;
    }

}