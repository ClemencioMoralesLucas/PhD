package phd.cml.particle.swarm.optimization;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by Clemencio Morales Lucas.
 */

public class ParticleTest {

    public static final double EXPECTED_FITNESS_VALUE = 5776089.22265625;
    private Particle particle;
    private PhysicalMagnitude velocity;
    private PhysicalMagnitude location;
    private static double [] VELOCITY_VALUES =  new double[] {2, 4, 3, 6};
    private static double [] LOCATION_VALUES =  new double[] {1, 7, 2, 9};

    @BeforeMethod
    public void setUp() {
        this.velocity = new Velocity(VELOCITY_VALUES);
        this.location = new Location(LOCATION_VALUES);
        this.particle = new Particle(EXPECTED_FITNESS_VALUE, this.velocity, this.location);
    }

    @Test
    public void testGetterAndSetterForVelocity() {
        final PhysicalMagnitude customVelocity = new Velocity(new double[]{2, 3, 2});
        this.particle.setVelocity(customVelocity);
        assertEquals(this.particle.getVelocity(), customVelocity);
    }

    @Test
    public void testGetterAndSetterForLocation() {
        final PhysicalMagnitude customLocation = new Location(new double[]{1, 0, 9, 2});
        this.particle.setLocation(customLocation);
        assertEquals(this.particle.getLocation(), customLocation);
    }

    @Test
    public void testGetFitnessValueAndEvaluate() {
        assertEquals(this.particle.getFitnessValue(), EXPECTED_FITNESS_VALUE);
    }

    @Test
    public void testEmptyParticleIsNotNull() {
        assertNotNull(new Particle());
    }
}
