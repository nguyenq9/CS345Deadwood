import java.util.ArrayList;

public class Set implements Location {
    private Scene scene;
    private ArrayList<Role> roles;
    private int maxShotCounter;
    private int shotCounters;
    private boolean wrapped;

    public Set(Scene scene, Role[] roles, int maxShotCounter, int shotCounters) {
        // this.wrapped = false;
    }

    public ArrayList<Location> getAdjacentLocations() {
        return null;
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

}