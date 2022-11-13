import java.util.ArrayList;

public class Set implements Location {
    private String setName;
    private Scene scene;
    private ArrayList<Role> roles;
    private int maxShotCounters;
    private int shotCounters;
    private boolean wrapped;
    private ArrayList<Location> adjLocations;   // Added adjLocation attribute
    private int[] locationArea;

    public Set(String setName, ArrayList<Role> roles, int maxShotCounters, int[] locationArea) {
        this.setName = setName;
        this.scene = null;
        this.roles = roles;
        this.maxShotCounters = maxShotCounters;
        this.shotCounters = maxShotCounters;
        this.adjLocations = null;
        this.locationArea = locationArea;
    }

    public ArrayList<Location> getAdjacentLocations() {
        return adjLocations;
    }

    public String getLocationName() {
        return setName;
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

    public int getMaxShotCounters() {
        return maxShotCounters;
    }

    public int getShotCounters() {
        return shotCounters;
    }

    public int[] getLocationArea() {
        return locationArea;
    }

    public void setAdjacentLocations(ArrayList<Location> locations) {
        adjLocations = locations;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void resetShotCounters() {
        shotCounters = maxShotCounters;
    }

	public void decrementShotCounters() {
        shotCounters--;
	}

}