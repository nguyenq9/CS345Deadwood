import java.util.ArrayList;

public class Scene {
    private int budget;
    private boolean visible;
    private ArrayList<Role> roles;
    private int sceneNumber;
    private String sceneImg;
    private String sceneName;
    private int[] area;

    public Scene(int budget, ArrayList<Role> roles) {
        this.budget = budget;
        this.roles = roles;
        this.visible = false;
    }

    public int getBudget() {
        return budget;
    }

    public boolean getVisible() {
        return visible;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public String sceneName() {
        return sceneName;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}