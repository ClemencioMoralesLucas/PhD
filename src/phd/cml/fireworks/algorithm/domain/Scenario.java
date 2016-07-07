package phd.cml.fireworks.algorithm.domain;

/**
 * Created by Clemencio Morales Lucas on 07/07/2016.
 */
public class Scenario {

    private Field field;

    public Scenario(final Field field){
        this.setField(field);
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
