package phd.cml.fireworks.algorithm.domain;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Clemencio Morales Lucas
 */
public class CoordinateTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Coordinate coordinate;
    private Coordinate otherCoordinate;

    public static final Integer W = 4;
    public static final Integer X = 6;
    public static final Integer Y = 8;
    public static final Integer Z = 10;

    @Before
    public void setUp(){
        this.coordinate = new Coordinate(W, X);
        this.otherCoordinate = new Coordinate(Y, Z);
    }

    @Test
    public void toStringTest() throws Exception {
        checkCoordinateToString(coordinate);
        checkCoordinateToString(otherCoordinate);
    }

    private void checkCoordinateToString(final Coordinate coordinate){
        String receivedToString = coordinate.toString();
        assertTrue(receivedToString.contains(coordinate.getX().toString()));
        assertTrue(receivedToString.contains(coordinate.getY().toString()));
    }

    @Test
    public void equalsTest() throws Exception {
        assertFalse(this.coordinate.equals(this.otherCoordinate));
        assertFalse(this.coordinate.equals(new Coordinate(X,Z)));
        assertTrue(this.coordinate.equals(this.coordinate));
        assertTrue(this.coordinate.equals(new Coordinate(W,X)));
        assertTrue(new Coordinate(Z, X).equals(new Coordinate(Z,X)));
        assertTrue(this.otherCoordinate.equals(this.otherCoordinate));
    }

    @Test
    public void hashCodeTest() throws Exception {
        assertTrue(this.coordinate.hashCode() == this.coordinate.hashCode());
        assertFalse(new Coordinate(Y, X).hashCode() == this.coordinate.hashCode());
        assertFalse(this.coordinate.hashCode() == this.otherCoordinate.hashCode());
    }

    @Test
    public void setNegativeCoordinateValueThrowException(){
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(Coordinate.COORDINATES_MUST_HAVE_POSITIVE_VALUES);
        new Coordinate(X * -1, Y);
    }
}