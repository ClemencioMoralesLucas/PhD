package phd.cml.fireworks.algorithm.domain.operators;

/**
 * Created by Clemencio Morales Lucas
 */
public class ExplosionOperator {

    private Integer strength;
    private Integer amplitude;
    private DisplacementOperator displacementOperator;

    public ExplosionOperator (final Integer strength, final Integer amplitude,
                              final DisplacementOperator displacementOperator){
        this.setStrength(strength);
        this.setAmplitude(amplitude);
        this.setDisplacementOperator(displacementOperator);
    }

    public void setStrength(final Integer strength){
        this.strength = strength;
    }

    public Integer getStrength(){
        return this.strength;
    }

    public void setAmplitude(final Integer amplitude){
        this.amplitude = amplitude;
    }

    public Integer getAmplitude(){
        return this.amplitude;
    }

    public void setDisplacementOperator(final
                                        DisplacementOperator displacementOperator){
        this.displacementOperator = displacementOperator;
    }

    public DisplacementOperator getDisplacementOperator(){
        return this.displacementOperator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExplosionOperator that = (ExplosionOperator) o;

        if (!strength.equals(that.strength)) return false;
        if (!amplitude.equals(that.amplitude)) return false;
        return displacementOperator.equals(that.displacementOperator);
    }

    @Override
    public int hashCode() {
        int result = strength.hashCode();
        result = 31 * result + amplitude.hashCode();
        result = 31 * result + displacementOperator.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ExplosionOperator{" +
                "strength=" + strength +
                ", amplitude=" + amplitude +
                ", displacementOperator=" + displacementOperator +
                '}';
    }
}
