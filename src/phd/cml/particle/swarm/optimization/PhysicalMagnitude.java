package phd.cml.particle.swarm.optimization;

/**
 * Created by Clemencio Morales Lucas.
 */

public abstract class PhysicalMagnitude {

    private double[] values;

    public PhysicalMagnitude(final double[] values) {
        this.setValues(values);
    }

    public double[] getValues() {
        return this.values;
    }

    public void setValues(final double[] values) {
        this.values = values;
    }
}
