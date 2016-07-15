package phd.cml.fireworks.algorithm.domain;

/**
 * Created by Clemencio Morales Lucas on 07/07/2016.
 */
public class Field {

    private Location[][] locations;

    public Field(Location[][] locations){
        this.setLocations(locations);
    }

    public Location[][] getLocations() {
        return locations;
    }

    public void setLocations(Location[][] locations) {
        this.locations = locations;
    }
}
