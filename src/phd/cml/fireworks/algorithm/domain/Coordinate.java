package phd.cml.fireworks.algorithm.domain;

/**
 * Created by Clemencio Morales Lucas on 07/07/2016.
 */
public class Coordinate {

    private int x;
    private int y;

    public Coordinate(final int x, final int y){
        this.setX(x);
        this.setY(y);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
