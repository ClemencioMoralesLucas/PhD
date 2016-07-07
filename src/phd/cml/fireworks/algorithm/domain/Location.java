package phd.cml.fireworks.algorithm.domain;

/**
 * Created by Clemencio Morales Lucas on 07/07/2016.
 */
public class Location {

    private Coordinate coordinate;
    private Spark spark;
    private Firework firework;
    private boolean optimal;

    public Location(final Coordinate coordinate){
        this.setCoordinate(coordinate);
        this.setSpark(null);
        this.setFirework(null);
        this.setOptimal(false);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public boolean isOptimal() {
        return optimal;
    }

    public void setOptimal(boolean optimal) {
        this.optimal = optimal;
    }

    public Firework getFirework() {
        return firework;
    }

    public void setFirework(Firework firework) {
        this.firework = firework;
    }

    public Spark getSpark() {
        return spark;
    }

    public void setSpark(Spark spark) {
        this.spark = spark;
    }
}
