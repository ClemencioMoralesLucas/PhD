package phd.cml.fireworks;

/**
 * Created by Clemencio Morales Lucas.
 */
public class Fireworks {
	
	public static void main(String[] args) {
		int numLocations = 5;
		int numMaxSparks = 50;
		double numBoundA = 0.04;
		double numBoundB = 0.8;
		double numMaxAmplitude = 40;
		int numGaussianSparks = 5;
		double [] maxbound ;//= new double [30];
		double [] minbound ;//= new double [30];
		int i;
		String infopath = "D:\\FA_info.txt";
		double[] shift_index2value={0, 0.05, 0.1, 0.2, 0.3, 0.5, 0.7};
		double[] bound = {100, 100, 30, 32, 600, 5.12, 50, 5, 2, 100, 5.12, 65.536};
		int[] dimension = {30,30,30,30,30,30,30,2,2,2,30,30};
		int repeatTimes = 20;

		System.out.print("average");
		for(int s = 0;s < shift_index2value.length;s++) {
			System.out.print(",shift_" + s);
		}
		System.out.println();
		//loop to run different parameters
		for(int idx = 1;idx <= 12;idx++) {
			System.out.print("fitness_" + idx);
			maxbound = new double[dimension[idx - 1]];
			minbound = new double[dimension[idx - 1]];
			for(i = 0;i < maxbound.length;i ++) {
				maxbound[i] = bound[idx - 1];
				minbound[i] = -bound[idx - 1];
			}
			for(int s = 0;s < shift_index2value.length;s++) {
				BenchmarkFunction func = new BenchmarkFunction();
				func.setIndexAndShift(idx, bound[idx - 1] * shift_index2value[s]);
				double avg = 0;
				for(int t = 0;t < repeatTimes;t++) {
					FireworkAlgorithm fao = new FireworkAlgorithm(numLocations,numMaxSparks,numBoundA,numBoundB,numMaxAmplitude,numGaussianSparks,maxbound,minbound,infopath,func);
					avg += fao.fireworksAlgorithmFramework();
				}
				avg /= repeatTimes;
				System.out.print("," + avg);
			}
			System.out.println();
		}
		
	}
	
}
