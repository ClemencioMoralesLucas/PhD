package phd.cml.fireworks;

import java.util.Arrays;

/**
 * Created by Clemencio Morales Lucas.
 */

public class Spark {

	private double [] position;
	private boolean evaluated;
	private double value;

	public Spark() {
		this.setEvaluated(false);
	}

	public double[] getPosition() {
		return this.position;
	}

	public void setPosition(final double [] receivedPosition) {
		this.position = new double [receivedPosition.length];
		for (int i = 0; i < receivedPosition.length; i++) {
			this.setPositionAtIndex(i, receivedPosition[i]);
		}
		this.setEvaluated(false);
	}

	public void setPositionAtIndex(final int index, final double value){
		this.getPosition()[index] = value;
	}

	public boolean isEvaluated() {
		return this.evaluated;
	}

	public void setEvaluated(final boolean evaluated) {
		this.evaluated = evaluated;
	}

	public double getValue(final BenchmarkFunction function) {
		if(!evaluated) {
			this.setEvaluated(true);
			value = function.getBenchmarkingFunctionValue(position);
			return value;
		} else {
			return value;
		}
	}

	public void setValue(final double value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Spark spark = (Spark) o;

		if (evaluated != spark.evaluated) return false;
		return Double.compare(spark.value, value) == 0 && Arrays.equals(position, spark.position);

	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = Arrays.hashCode(position);
		result = 31 * result + (evaluated ? 1 : 0);
		temp = Double.doubleToLongBits(value);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "Spark{" +
				"position=" + Arrays.toString(position) +
				", evaluated=" + evaluated +
				", value=" + value +
				'}';
	}
}