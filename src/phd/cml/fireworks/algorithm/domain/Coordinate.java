package phd.cml.fireworks.algorithm.domain;

/**
 * Created by Clemencio Morales Lucas
 */
public class Coordinate {

    public static final String COORDINATES_MUST_HAVE_POSITIVE_VALUES = "Coordinates must have positive values.";
    private Integer x;
    private Integer y;

    public Coordinate(final Integer x, final Integer y){
        this.setX(x);
        this.setY(y);
    }

    public Integer getX() {
        return x;
    }

    public void setX(final Integer x) {
        if(x < 0){
            throw new IllegalArgumentException(COORDINATES_MUST_HAVE_POSITIVE_VALUES);
        } else {
            this.x = x;
        }
    }

    public Integer getY() {
        return y;
    }

    public void setY(final Integer y) {
        if(y < 0){
            throw new IllegalArgumentException(COORDINATES_MUST_HAVE_POSITIVE_VALUES);
        } else {
            this.y = y;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (!x.equals(that.x)) return false;
        return y.equals(that.y);

    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
