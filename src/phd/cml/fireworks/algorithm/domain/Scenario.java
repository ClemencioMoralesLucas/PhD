package phd.cml.fireworks.algorithm.domain;

import java.util.Random;

/**
 * Created by Clemencio Morales Lucas on 07/07/2016.
 */
public class Scenario {

    private Field field;

    public Scenario(final Field field){
        this.setField(field);
    }

    public void createScenario(final int height, final int width){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                this.getField().getLocations()[i][j] = createRandomCoordinate(width);
            }
        }
        //An optimal coordinate is created representing the objective point
        this.getField().getLocations()[this.generateRandomValue(width)][this.generateRandomValue(width)].setOptimal(true);
    }

    private Location createRandomCoordinate(final int maxBound) {
        int x = generateRandomValue(maxBound);
        int y = generateRandomValue(maxBound);
        return new Location(new Coordinate(x, y));

    }

    //TODO Move to utils.class
    private int generateRandomValue(final int maxBound){
        return new Random().nextInt(maxBound);
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
