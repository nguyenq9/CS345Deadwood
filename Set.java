import java.util.ArrayList;

public class Set implements Location {
    private String setName;
    private Scene scene;
    private ArrayList<Role> roles;
    private int maxShotCounters;
    private int shotCounters;
    private boolean isWrapped;
    private ArrayList<Location> adjLocations;   // Added adjLocation attribute
    private int[] locationArea;
    private Bank bank = Bank.bank;

    public Set(String setName, ArrayList<Role> roles, int maxShotCounters, int[] locationArea) {
        this.setName = setName;
        this.scene = null;
        this.roles = roles;
        this.maxShotCounters = maxShotCounters;
        this.shotCounters = maxShotCounters;
        this.adjLocations = null;
        this.locationArea = locationArea;
    }

    // wraps a scene and resets the players who had roles in the set
    public void wrapScene() {
        setIsWrapped(true);

        ArrayList<Role> sceneRoles = getScene().getRoles();
        for (int i = 0; i < sceneRoles.size(); i++) {
            if (sceneRoles.get(i).getActor() != null) {
                bank.payBonusRewards(this);
                break;
            }
        }

        for (int i = 0; i < getRoles().size(); i++) {
            PlayerController player = getRoles().get(i).getActor();
            if (player != null) {
                getRoles().get(i).setActor(null);
                player.setPlayerIsWorking(false);
                player.setPlayerRole(null);
                player.setPlayerSet(null);
                player.setPlayerRehearsals(0);
            }
        }

        for (int i = 0; i < sceneRoles.size(); i++) {
            PlayerController player = sceneRoles.get(i).getActor();
            if (player != null) {
                sceneRoles.get(i).setActor(null);
                player.setPlayerIsWorking(false);
                player.setPlayerRole(null);
                player.setPlayerSet(null);
                player.setPlayerRehearsals(0);
            }
        }
        
        Deadwood.decrementActiveScenes();
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

    public boolean getIsWrapped() {
        return isWrapped;
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

    public void setIsWrapped(boolean isWrapped) {
        this.isWrapped = isWrapped;
    }

    public void resetShotCounters() {
        shotCounters = maxShotCounters;
    }

	public void decrementShotCounters() {
        shotCounters--;
	}

}