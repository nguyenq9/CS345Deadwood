import java.util.ArrayList;

public class Board{
    private ArrayList<Location> locations;
    private ArrayList<Set> sets;
    private Trailer trailer;
    private CastingOffice office;
    
    public Board (ArrayList<Location> locations, ArrayList<Set> sets, Trailer trailer, CastingOffice office) {
        this.locations = locations;
        this.sets = sets;
        this.trailer = trailer;
        this.office = office;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public ArrayList<Set> getSets() {
        return sets;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public CastingOffice getOffice() {
        return office;
    }
}