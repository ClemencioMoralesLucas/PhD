package phd.cml.fireworks.algorithm.domain;

import phd.cml.fireworks.algorithm.utils.FireworksAlgorithmUtils;

/**
 * Created by Clemencio Morales Lucas
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
        this.getField().getLocations()
                [FireworksAlgorithmUtils.generateRandomValue(width)]
                [FireworksAlgorithmUtils.generateRandomValue(width)].setOptimal(true);
    }

    private Location createRandomCoordinate(final int maxBound) {
        int x = FireworksAlgorithmUtils.generateRandomValue(maxBound);
        int y = FireworksAlgorithmUtils.generateRandomValue(maxBound);
        return new Location(new Coordinate(x, y));
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
