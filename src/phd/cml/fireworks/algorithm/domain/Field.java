package phd.cml.fireworks.algorithm.domain;

import java.util.Arrays;

/**
 * Created by Clemencio Morales Lucas
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        return Arrays.deepEquals(locations, field.locations);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(locations);
    }

    @Override
    public String toString() {
        return "Field{" +
                "locations=" + Arrays.toString(locations) +
                '}';
    }
}
