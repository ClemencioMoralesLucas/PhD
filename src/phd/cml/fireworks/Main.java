package phd.cml.fireworks;

/**
 * Created by Clemencio Morales Lucas.
 */
public class Main {

    public static final String INFORMATION_FILE_PATH_WINDOWS = "D:\\FA_info.txt";
    public static final String INFORMATION_FILE_PATH_UNIX = "/home/clemen/FA_info.txt";
    public static final int NUMBER_OF_ITERATIONS = 20;
    public static final int LOCATIONS_NUMBER = 5;
    public static final int NUMBER_OF_SPARKS = 50;
    public static final double LOW_BOUND_NUMBER = 0.04;
    public static final double HIGH_BOUND_NUMBER = 0.8;
    public static final int MAXIMUM_AMPLITUDE_VALUE = 40;
    public static final int GAUSSIAN_SPARKS_VALUE = 5;
    public static final String AVERAGE = "Average values: ";
    public static final String SHIFT = "|Shift ";
    public static final String SEPARATOR = "|\t";
    public static final String FITNESS = "|Fitness ";
    public enum HostSystem { WINDOWS, UNIX }

    private double[] maximumBound;//= new double [30];
    private double[] minimumBound;//= new double [30];
    private final double[] shiftIndex = {0, 0.05, 0.1, 0.2, 0.3, 0.5, 0.7};
    private double[] availableBounds = {100, 100, 30, 32, 600, 5.12, 50, 5, 2, 100, 5.12, 65.536};
    private int[] availableDimensions = {30, 30, 30, 30, 30, 30, 30, 2, 2, 2, 30, 30};
    private String filePath;

    public Main() {}

    public void launch(final HostSystem hostSystem) {
        filePath = hostSystem.equals(HostSystem.UNIX) ? INFORMATION_FILE_PATH_UNIX : INFORMATION_FILE_PATH_WINDOWS;
        printHeader();
        launchFWAForAllParameters();
    }

    private void launchFWAForAllParameters() {
        for (int i = 1; i <= BenchmarkFunctionConstants.NUMBER_OF_FUNCTIONS; i++) {
            System.out.print(FITNESS + i + SEPARATOR);
            maximumBound = new double[availableDimensions[i - 1]];
            minimumBound = new double[availableDimensions[i - 1]];
            for (int j = 0; j < maximumBound.length; j++) {
                maximumBound[j] = availableBounds[i - 1];
                minimumBound[j] = -availableBounds[i - 1];
            }
            for (int k = 0; k < shiftIndex.length; k++) {
                BenchmarkFunction benchmarkFunction = new BenchmarkFunction();
                benchmarkFunction.setIndexAndShift(i, availableBounds[i - 1] * shiftIndex[k]);
                double avg = 0;
                for (int t = 0; t < NUMBER_OF_ITERATIONS; t++) {
                    FireworkAlgorithm fireworkAlgorithm = new FireworkAlgorithm(LOCATIONS_NUMBER, NUMBER_OF_SPARKS,
                            LOW_BOUND_NUMBER, HIGH_BOUND_NUMBER, MAXIMUM_AMPLITUDE_VALUE, GAUSSIAN_SPARKS_VALUE,
                            maximumBound, minimumBound, filePath, benchmarkFunction);
                    avg += fireworkAlgorithm.launch();
                }
                avg /= NUMBER_OF_ITERATIONS;
                System.out.print("\t" + avg);
            }
            System.out.println();
        }
    }

    private void printHeader() {
        System.out.print(AVERAGE);
        for (int s = 0; s < shiftIndex.length; s++) {
            System.out.print(SHIFT + s + SEPARATOR);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        //TODO Find a way of reformatting the table
        long initialTime = System.nanoTime();
        new Main().launch(HostSystem.WINDOWS);
        long finalTime = System.nanoTime();
        System.out.println("Time elapsed for current algorithm: "+ (finalTime-initialTime));

        //TODO We can go this way roughly
//        long initialTimeImp = System.nanoTime();
//        new Main().launchImprovedVersion(HostSystem.WINDOWS);
//        long finalTimeImp = System.nanoTime();
//        System.out.println("Time elapsed for improved algorithm: "+ (finalTimeImp-initialTimeImp));
    }
}
