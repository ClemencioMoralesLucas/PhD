package phd.cml.fireworks.algorithm.domain;

/**
 * Created by Clemencio Morales Lucas on 07/07/2016.
 */
public class Field {

    private Location[] field;

    public Field(Location[] locations){
        this.setField(locations);
    }

    public Location[] getField() {
        return field;
    }

    public void setField(Location[] field) {
        this.field = field;
    }
}
