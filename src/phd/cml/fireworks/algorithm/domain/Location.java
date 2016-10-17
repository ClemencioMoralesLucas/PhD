package phd.cml.fireworks.algorithm.domain;

/**
 * Created by Clemencio Morales Lucas
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

    public Location(final Coordinate coordinate, final Spark spark,
                    final Firework firework, final boolean optimal){
        this.setCoordinate(coordinate);
        this.setSpark(spark);
        this.setFirework(firework);
        this.setOptimal(optimal);
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

    @Override
    public String toString() {
        return "Location{" +
                "coordinate=" + coordinate +
                ", spark=" + spark +
                ", firework=" + firework +
                ", optimal=" + optimal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (optimal != location.optimal) return false;
        if (!coordinate.equals(location.coordinate)) return false;
        if (!spark.equals(location.spark)) return false;
        return firework.equals(location.firework);

    }

    @Override
    public int hashCode() {
        int result = coordinate.hashCode();
        result = 31 * result + spark.hashCode();
        result = 31 * result + firework.hashCode();
        result = 31 * result + (optimal ? 1 : 0);
        return result;
    }
}
