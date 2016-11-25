package phd.cml.fireworks;

/**
 * Created by Clemencio Morales Lucas.
 */
public class BenchmarkFunction implements BenchmarkFunctionConstants {

	private int index;
	private double shift;

	public int getIndex() {
		return this.index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public double getShift() {
		return this.shift;
	}

	public void setShift(final double shift) {
		this.shift = shift;
	}

	public void setIndexAndShift(final int index, final double shift) {
		this.setIndex(index);
		this.setShift(shift);
	}

	public double getBenchmarkingFunctionValue(double [] inputValues) {
		double result = 0;
		inputValues = incrementInputValuesByShift(inputValues);
		switch (index) {
			case 1:
				result = sphereParabolaFunction(inputValues);
				break;
			case 2:
				result = schwefelFunction(inputValues);
				break;
			case 3:
				result = rosenbrockFunction(inputValues);
				break;
			case 4:
				result = ackleyFunction(inputValues);
				break;
			case 5:
				result = griewanksFunction(inputValues);
				break;
			case 6:
				result = rastriginFunction(inputValues);
				break;
			case 7:
				result = penalizedBenchmarkFunction(inputValues);
				break;
			case 8:
				result = sixHumpCamelBackFunction(inputValues);
				break;
			case 9:
				result = goldsteinFunction(inputValues);
				break;
			case 10:
				result = schafferFunction(inputValues);
				break;
			case 11:
				result = axisParallelFunction(inputValues);
				break;
			case NUMBER_OF_FUNCTIONS: //Defines the number of benchmark functions.
				result = rotatedHyperEllipsoidFunction(inputValues);
				break;
			default:
				System.err.println(INVALID_FUNCTION_INDEX + index);
				System.exit(ERROR_CODE_STATUS);
		}
		return result;
	}

	private double rotatedHyperEllipsoidFunction(final double[] inputValues) {
		//Rotated hyper ellipsoid Function Bounds[-65.536,65.536] dim 30 optimum 0.0D ini [32.768,65.536]
		double aux, result = 0;
		for (int i = 0; i < inputValues.length; i++) {
            aux = 0;
            for (int j = 0; j < i; j++) {
                aux = aux + inputValues[j];
            }
            result = result + aux * aux;
        }
		return result;
	}

	private double axisParallelFunction(final double[] inputValues) {
		//Axis parallel hyper ellipsoid Function Bounds[-5.12,5.12] dim 30 optimum 0.0D ini [2.56,5.12]
		double result = 0;
		for (int i = 0; i < inputValues.length; i++) {
            result = result + i * inputValues[i] * inputValues[i];
        }
		return result;
	}

	private double schafferFunction(final double[] inputValues) {
		//Schaffer Function Bounds[-100,100] dim2 optimum=0D ini [50,100]
		return SCHAFFER_HALF + (Math.pow(Math.sin(Math.sqrt(inputValues[0] * inputValues[0] +
				inputValues[1] * inputValues[1])), SQUARE_VALUE) - SCHAFFER_HALF) /
				Math.pow(DOUBLE_IDENTITY_VALUE + SCHAFFER_THOUSANDTH * (inputValues[0] * inputValues[0] +
						inputValues[1] * inputValues[1]), SQUARE_VALUE);
	}

	private double goldsteinFunction(final double[] inputValues) {
		//Goldstein Function Bounds[-2,2]  dim2 optimaum=(0,-1) ini[1,2]
		double result = IDENTITY_VALUE + Math.pow(inputValues[0] + inputValues[1] + 1, SQUARE_VALUE) *
				(GOLDSTEIN_NINETEEN - GOLDSTEIN_FOURTEEN * inputValues[0] + GOLDSTEIN_THREE * 
						inputValues[0] * inputValues[0] - GOLDSTEIN_FOURTEEN * inputValues[1] + 
						GOLDSTEIN_SIX * inputValues[0] * inputValues[1] + GOLDSTEIN_THREE * inputValues[1] *
						inputValues[1]);
		result = result * (GOLDSTEIN_THIRTY + Math.pow(SQUARE_VALUE * inputValues[0] - GOLDSTEIN_THREE * 
				inputValues[1], SQUARE_VALUE) * (GOLDSTEIN_EIGHTEEN - GOLDSTEIN_THIRTYTWO * inputValues[0] + 
				GOLDSTEIN_TWELVE * inputValues[0] * inputValues[0] + GOLDSTEIN_FOURTYEIGHT * inputValues[1] -
				GOLDSTEIN_THIRTYSIX * inputValues[0] * inputValues[1] + GOLDSTEIN_TWENTYSEVEN * inputValues[1] *
				inputValues[1]));
		return result;
	}

	private double sixHumpCamelBackFunction(final double[] inputValues) {
		//Six Hump Camel-back Bounds[-5,5]  dim2 optimum=(-0.0898,0.7126),(0.0898,-0.7126) ini[25,50]
		return  SIX_HUMP_CAMEL_BACK_FOUR * Math.pow(inputValues[0], DOUBLE_SQUARE_VALUE) -
				SIX_HUMP_CAMEL_BACK_FUNCTION_CONST * Math.pow(inputValues[0],
						SIX_HUMP_CAMEL_BACK_FOUR_DOUBLE) + DOUBLE_IDENTITY_VALUE /
				SIX_HUMP_CAMEL_BACK_THREE_DOUBLE * Math.pow(inputValues[0],
				SIX_HUMP_CAMEL_BACK_SIX) + inputValues[0] * inputValues[1] -
				SIX_HUMP_CAMEL_BACK_FOUR * Math.pow(inputValues[1], SQUARE_VALUE) +
				SIX_HUMP_CAMEL_BACK_FOUR * Math.pow(inputValues[1], SIX_HUMP_CAMEL_BACK_FOUR);
	}

	private double penalizedBenchmarkFunction(final double[] inputValues) {
		//Penalized Function Bounds[-50,50] dim30 optimum=1D ini[25,50]
		double aux = 0, result = 0;
		int i;
		for (i = 0; i < inputValues.length; i++) {
            aux = aux + miu_benchmark(inputValues[i], MIU_BOUND, MIU_MULTIPLIER_FACTOR, MIU_POWER);
        }
		for (i = 0; i < inputValues.length - 1; i++) {
            result = result + Math.pow(inputValues[i] - 1, SQUARE_VALUE) * (IDENTITY_VALUE +
					Math.pow(Math.sin(PENALIZED_BENCHMARK_FUNCTION_SIN_VALUE  * Math.PI *
							inputValues[i + 1]), SQUARE_VALUE));
        }
		result = result + Math.pow(Math.sin(PENALIZED_BENCHMARK_FUNCTION_SIN_VALUE * Math.PI *
				inputValues[0]), DOUBLE_SQUARE_VALUE);
		result = result + Math.pow(inputValues[inputValues.length - 1] - IDENTITY_VALUE,
				SQUARE_VALUE) * (IDENTITY_VALUE + Math.pow(Math.sin(SQUARE_VALUE * Math.PI *
				inputValues[inputValues.length - 1]), SQUARE_VALUE));
		result = result * PENALIZED_BENCHMARK_FUNCTION_MULTIPLIER;
		return result + aux;
	}

	private double rastriginFunction(final double[] inputValues) {
		//Generalized Rastrigin Function Bounds[-5.12,5.12]; dim30 optimum=0D ini[2.56,5.12]
		double aux = 0, result = 0;
		for (int i = 0; i < inputValues.length; i++) {
            result += Math.pow(inputValues[i], DOUBLE_SQUARE_VALUE);
            aux += Math.cos(SQUARE_VALUE * Math.PI * inputValues[i]);
        }
		result += RASTRIGIN_LOWER_BOUND * aux + RASTRIGIN_UPPER_BOUND * inputValues.length;
		return result;
	}

	private double griewanksFunction(final double[] inputValues) {
		//Griewank's Function Bounds[-600,600] dim30 optimum=0D ini[300,600]
		double aux = DOUBLE_IDENTITY_VALUE, result = 0;
		for (int i = 0; i < inputValues.length; i++) {
            result += Math.pow(inputValues[i], DOUBLE_SQUARE_VALUE);
            aux *= Math.cos(inputValues[i] / Math.sqrt(i + DOUBLE_IDENTITY_VALUE));
        }
		result /= GRIEWANKS_CONSTANT_DIVIDER;
		result += IDENTITY_VALUE - aux;
		return result;
	}

	private double ackleyFunction(final double[] inputValues) {
		//Ackley Function Bounds[-32,32]   dim30 optimum=0D ini[16,32]
		double aux = 0, result = 0;
		for (int i = 0; i < inputValues.length; i++) {
            result += Math.pow(inputValues[i], DOUBLE_SQUARE_VALUE);
            aux += Math.cos(SQUARE_VALUE * Math.PI * inputValues[i]);
        }
		result = ACKLEY_FUNCTION_NEGATIVE_MULTIPLIER * Math.sqrt(result / inputValues.length);
		result = ACKLEY_FUNCTION_LOWER_BOUND * Math.exp(result);

		aux = -Math.exp(aux / inputValues.length);

		result += ACKLEY_FUNCTION_UPPER_BOUND + Math.exp(DOUBLE_IDENTITY_VALUE) + aux;
		return result;
	}

	private double rosenbrockFunction(final double[] inputValues) {
		//Generalized Rosenbrock Function Bounds[-30,30]   dim30 optimum=1D ini[15,30]
		double result = 0;
		for (int i = 0; i < inputValues.length - 1; i++) {
            result = result + ONE_HUNDRED * Math.pow(inputValues[i + 1] - inputValues[i] *
					inputValues[i], DOUBLE_SQUARE_VALUE) + Math.pow(inputValues[i] - 1, DOUBLE_SQUARE_VALUE);
        }
		return result;
	}

	private double schwefelFunction(final double[] inputValues) {
		//Schwefel Function Bounds[-100,100] dim30 optimum=0D ini[50,100]
		double aux, result = 0;
		for (int i = 0; i < inputValues.length; i++) {
            aux = 0;
            for (int j = 0; j < i; j++) {
                aux = Math.pow(inputValues[j], SQUARE_VALUE) + aux;
            }
            result += aux;
        }
		return result;
	}

	private double sphereParabolaFunction(final double[] inputValues) {
		//Sphere Parabola Function Bounds[-100,100] dim30 optimum=0D ini[50,100]
		double result = 0;
		for (int i = 0; i < inputValues.length; i++) {
            result += Math.pow(inputValues[i], SQUARE_VALUE);
        }
		return result;
	}

	private double[] incrementInputValuesByShift(final double [] inputValues){
		for (int i = 0; i < inputValues.length; i++){
			inputValues[i] += this.getShift();
		}
		return  inputValues;
	}
	
	private double miu_benchmark (final double pivotValue, final double bound,
								  final double multiplierFactor, final double power) {
		double fit;
		if( pivotValue > bound ) {
			fit = multiplierFactor * Math.pow(pivotValue-bound,power);
		} else if(pivotValue > -bound) {
			fit = 0;
		} else {
			fit = multiplierFactor * Math.pow(-pivotValue-bound, power);
		}
		return fit;
	}
}
