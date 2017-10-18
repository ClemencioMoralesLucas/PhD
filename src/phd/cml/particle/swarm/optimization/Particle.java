package phd.cml.particle.swarm.optimization;

/**
 * Created by Clemencio Morales Lucas.
 */

public class Particle {

    public static final double LOCATION_X_LOW = 1;
    public static final double LOCATION_X_HIGH = 4;

    public static final double LOCATION_Y_LOW = -1;
    public static final double LOCATION_Y_HIGH = 1;

    public static final double VELOCITY_LOW = -1;
    public static final double VELOCITY_HIGH = 1;

    private double fitnessValue;
    private PhysicalMagnitude velocity;
    private PhysicalMagnitude location;

    public Particle(final double fitnessValue, final PhysicalMagnitude velocity, final PhysicalMagnitude location) {
        this.fitnessValue = fitnessValue;
        this.velocity = velocity;
        this.location = location;
    }

    public Particle(){}

    public PhysicalMagnitude getVelocity() {
        return this.velocity;
    }

    public void setVelocity(final PhysicalMagnitude velocity) {
        this.velocity = velocity;
    }

    public PhysicalMagnitude getLocation() {
        return this.location;
    }

    public void setLocation(final PhysicalMagnitude location) {
        this.location = location;
    }

    public double getFitnessValue() {
        fitnessValue = evaluate(location);
        return fitnessValue;
    }

    /*
    This evaluation function solves the following problem:
    Find an �x� and �y� value that minimize the function below:
    f(x, y) = (2.8125 - x + x * y^4)^2 + (2.25 - x + x * y^2)^2 + (1.5 - x + x*y)^2
    Where 1 <= x <= 4, and -1 <= y <= 1
     */
    public static double evaluate(final PhysicalMagnitude location) {
        double result;
        final double x = location.getValues()[0];
        final double y = location.getValues()[1];

        result = Math.pow(2.8125 - x + x * Math.pow(y, 4), 2) +
                Math.pow(2.25 - x + x * Math.pow(y, 2), 2) +
                Math.pow(1.5 - x + x * y, 2);

        return result;
    }
}
