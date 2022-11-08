import java.util.ArrayList;

public class Scene {
    private int budget;
    private boolean visible;
    private ArrayList<Role> roles;
    private int sceneNumber;
    private String sceneImg;
    private String sceneName;
    private String sceneDescription;

    public Scene(int budget, ArrayList<Role> roles, int sceneNumber, String sceneImg, String sceneName, String sceneDescription) {
        this.budget = budget;
        this.roles = roles;
        this.visible = false;
        this.sceneNumber = sceneNumber;
        this.sceneImg = sceneImg;
        this.sceneName = sceneName;
        this.sceneDescription = sceneDescription;
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

    public String getSceneName() {
        return sceneName;
    }

    public int getSceneNumber() {
        return sceneNumber;
    }
    public String getSceneImg() {
        return sceneImg;
    }

    public String getSceneDescription() {
        return sceneDescription;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}