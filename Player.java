public class Player {
    private String name;
    private int dollars;
    private int credits;
    private int rank;
    private int rehearsals;
    private boolean isWorking;
    private Role role;
    private Set set;
    private Location location;

    // Default number of players
    public Player(String name, Location startingLocation) {
        this.name = name;
        this.dollars = 0;
        this.credits = 0;
        this.rank = 1;
        this.rehearsals = 0;
        this.isWorking = false;
        this.role = null;
        this.set = null;
        this.location = startingLocation;
    }

    public Player(String name, int credits, int rank, Location startingLocation) {
        this.name = name;
        this.dollars = 0;
        this.credits = credits;
        this.rank = rank;
        this.rehearsals = 0;
        this.isWorking = false;
        this.role = null;
        this.set = null;
        this.location = startingLocation;
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

    public int getRehearsals() {
        return rehearsals;
    }

    public Role getRole() {
        return role;
    }

    public Set getSet() {
        return set;
    }

    public boolean getIsWorking() {
        return isWorking;
    }

    public Location getLocation() {
        return location;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setDollars(int newDollars) {
        this.dollars = newDollars;
    }

    public void addDollars(int amount) {
        this.dollars += amount;
    }

    public void deductDollars(int amount) {
        this.dollars -= amount;
    }

    public void setCredits(int newCredits) {
        this.credits = newCredits;
    }

    public void addCredits(int amount) {
        this.credits += amount;
    }

    public void deductCredits(int amount) {
        this.credits -= amount;
    }

    public void setRank(int newRank) {
        this.rank = newRank;
    }

    public void setRehearsals(int rehearsals) {
        this.rehearsals = rehearsals;
    }

    public void incrementRehearsals() {
        rehearsals++;
    }

    public void setRole(Role newRole) {
        this.role = newRole;
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public void setLocation(Location newLocation) {
        this.location = newLocation;
    }

    public void setIsWorking(boolean isWorking) {
        this.isWorking = isWorking;
    }
}