public class Role{
    private int rank;
    private boolean onCard;
    private boolean isTaken;
    private PlayerController actor;
    private String roleName;
    private String roleLine;
    private int[] area;

    public Role(int rank, boolean onCard, String roleName, String roleLine) {
        this.rank = rank;
        this.onCard = onCard;
        this.roleLine = roleLine;
        this.roleName = roleName;
    }

    public int getRank() {
        return rank;
    }

    public boolean getOnCard() {
        return onCard;
    }

    public boolean getTaken() {
        return isTaken;
    }

    public PlayerController getActor() {
        return actor;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getRoleLine() {
        return roleLine;
    }

    public int[] getArea() {
        return area;
    }

    public void setIsTaken(boolean isTaken) {
        this.isTaken = isTaken;
    }

    public void setActor(PlayerController actor) {
        this.actor = actor;
    }

}