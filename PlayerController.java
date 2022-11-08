public class PlayerController {
    private Player player;
    private PlayerView playerView;

    public PlayerController(Player player, PlayerView playerView) {
        this.player = player;
        this.playerView = playerView;
    }

    public String getPlayerName(){
        return player.getName();
    }

    public int getPlayerDollars() {
        return player.getDollars();
    }

    public int getPlayerCredits() {
        return player.getCredits();
    }

    public int getPlayerRank() {
        return player.getRank();
    }

    public int getPlayerPracChip() {
        return player.getPracChip();
    }

    public Role getPlayerCurrRole() {
        return player.getCurrRole();
    }

    public Location getPlayerCurrLocation() {
        return player.getCurrLocation();
    }

    public void setPlayerName(String newName) {
        player.setName(newName);
    }

    public void setPlayerDollars(int newDollars) {
        player.setDollars(newDollars);
    }

    public void setPlayerCredits(int newCredits) {
        player.setCredits(newCredits);
    }

    public void setPlayerRank(int newRank) {
        player.setRank(newRank);
    }

    public void setPlayerPracChip(int newPracChip) {
        player.setPracChip(newPracChip);
    }

    public void setPlayerCurrRole(Role newRole) {
        player.setCurrRole(newRole);
    }

    public void setPlayerCurrLocation(Location newLocation) {
        player.setCurrLocation(newLocation);
    }

}