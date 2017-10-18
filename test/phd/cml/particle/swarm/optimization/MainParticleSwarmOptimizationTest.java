package phd.cml.particle.swarm.optimization;

import org.testng.annotations.Test;

import static org.testng.Assert.fail;

/**
 * Created by Clemencio Morales Lucas.
 */

public class MainParticleSwarmOptimizationTest {

    @Test
    public void testMain() throws Exception {
        try {
            Main.main(null);
        } catch (final Exception exception) {
            fail();
        }
    }
}
