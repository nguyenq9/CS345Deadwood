public class Player {
    private String name;
    private int dollars;
    private int credits;
    private int rank;
    private int pracChip;
    private Role currRole;
    private Location currLocation;

    // Default number of players
    public Player(String name) {
        this.name = name;
        this.dollars = 0;
        this.credits = 0;
        this.rank = 1;
        this.pracChip = 0;
        // this.currRole = null;
        // this.currLocation = trailer;
    }

    public Player(String name, int credits, int rank) {
        this.name = name;
        this.dollars = 0;
        this.credits= credits;
        this.rank = rank;
        this.pracChip = 0;
        this.currRole = null;
        // Trailer trailer = new Trailer();
        // this.currLocation = trailer;
    }

    public String getName(){        
        return name;
    }

    public int getDollars() {
        return dollars;
    }

    public int getCredits() {
        return credits;
    }

    public int getRank() {
        return rank;
    }

    public int getPracChip() {
        return pracChip;
    }

    public Role getCurrRole() {
        return currRole;
    }

    public Location getCurrLocation() {
        return currLocation;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setDollars(int newDollars) {
        this.dollars = newDollars;
    }

    public void setCredits(int newCredits) {
        this.credits = newCredits;
    }

    public void setRank(int newRank) {
        this.rank = newRank;
    }

    public void setPracChip(int newPracChip) {
        this.pracChip = newPracChip;
    }
    public void setCurrRole(Role newRole) {
        this.currRole = newRole;
    }

    public void setCurrLocation(Location newLocation) {
        this.currLocation = newLocation;
    }
}