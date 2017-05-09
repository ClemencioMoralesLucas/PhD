package phd.cml.fireworks;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
 * Created by Clemencio Morales Lucas.
 */

//TODO README: This class is, by now, a duplicate of FireworkAlgorithm.java. The idea is to make the improvements here, everything is set to launch both executions and compare them.
public class ImprovedFireworkAlgorithm {

    private static final double EPS_VALUE = 1e-38;
    private static final double QUARTER = 0.25;
    private static final double HALF = 0.5;
    private static final int DOUBLE_COEFFICIENT = 2;
    private static final double GAUSSIAN_COEFFICIENT_BASE = 1.0;
    private static final String BEST_VALUE_FOUND = "Best value found ==> ";
    private static final String BEST_POSITION_FOUND = "best position found ==> ";
    private static final String SEPARATOR = "---------------------------------------------------------";
    private static final String OUTPUT_FILE_NOT_FOUND_IN_PATH = ": Output file not found in:";
    public static final int NUMBER_OF_FUNCTION_EVALUATIONS = 30;

    private Spark[] fireworks;
    private Spark[][] sparks;
    private Spark[] gaussianSparks;
    private int locationsNumber;
    private int maximumSparksNumber;
    private double lowBound;
    private double highBound;
    private double maximumAmplitudeNumber;
    private int gaussianSparksNumber;
    private double[] maximumBound;
    private double[] minimalBound;
    private int dimension;
    private String infoFilePath;
    private int numGenerations;
    private double optimumValue;
    private int numFunctionEvaluations;
    BenchmarkFunction benchmarkFunction;

    public ImprovedFireworkAlgorithm(final int locationsNumber, final int maximumSparksNumber, final double lowBound,
                                     final double highBound, final double maximumAmplitudeNumber, final int gaussianSparksNumber,
                                     final double[] maximumBound, final double[] minimalBound, final String infoFilePath,
                                     final BenchmarkFunction benchmarkFunction) {
        this.locationsNumber = locationsNumber;
        this.maximumSparksNumber = maximumSparksNumber;
        this.lowBound = lowBound;
        this.highBound = highBound;
        this.maximumAmplitudeNumber = maximumAmplitudeNumber;
        this.gaussianSparksNumber = gaussianSparksNumber;
        this.maximumBound = maximumBound;
        this.minimalBound = minimalBound;
        this.dimension = this.maximumBound.length;
        this.infoFilePath = infoFilePath;
        this.benchmarkFunction = benchmarkFunction;
    }

    public double launch() {
        selectNInitialLocations();
        while (!stopCriteria()) {
            setOffNFireworks();
            selectNLocations();
        }
        return optimumValue;
    }

    private void selectNInitialLocations() {
        numGenerations = 0;
        numFunctionEvaluations = 0;
        fireworks = new Spark[locationsNumber];
        double[] randomPosition = new double[dimension];

        for (int i = 0; i < locationsNumber; i++) {
            fireworks[i] = new Spark();
            for (int j = 0; j < dimension; j++) {
                randomPosition[j] = maximumBound[j] - Math.random() * (maximumBound[j] - minimalBound[j]) * QUARTER;
            }
            fireworks[i].setPosition(randomPosition);
        }
    }

    private void setOffNFireworks() {
        numGenerations++;
        //get max(worst) and min(best) value
        double maximumValue = fireworks[0].getValue(benchmarkFunction);
        double minimumValue = fireworks[0].getValue(benchmarkFunction);
        int i;
        for (i = 1; i < locationsNumber; i++) {
            if (fireworks[i].getValue(benchmarkFunction) > maximumValue) {
                maximumValue = fireworks[i].getValue(benchmarkFunction);
            }
            if (fireworks[i].getValue(benchmarkFunction) < minimumValue) {
                minimumValue = fireworks[i].getValue(benchmarkFunction);
            }
        }
        double sumMaxDiff = 0.0;
        double sumMinDiff = 0.0;
        for (i = 0; i < locationsNumber; i++) {
            sumMaxDiff += maximumValue - fireworks[i].getValue(benchmarkFunction);
            sumMinDiff += fireworks[i].getValue(benchmarkFunction) - minimumValue;
        }

        int[] sparksNumber = new int[locationsNumber];
        getNumberOfSparksForAllFireworks(maximumValue, sumMaxDiff, sparksNumber);

        double explosionAmplitude[] = new double[locationsNumber];
        getExplosionAmplitudeForAllFireworks(minimumValue, sumMinDiff, explosionAmplitude);

        sparks = new Spark[locationsNumber][];
        double[] temporalPosition = generateSparksForAllFireworks(sparksNumber, explosionAmplitude);

        gaussianSparks = new Spark[gaussianSparksNumber];
        gaussianExplode(temporalPosition);
    }

    //todo changing the explosion type may throw different results
    private void gaussianExplode(double[] temporalPosition) {
        int i;
        double[] fireworkPosition;
        int k, j;
        Random rand;
        for (k = 0; k < gaussianSparksNumber; k++) {
            gaussianSparks[k] = new Spark();
            //randomly select a firework
            rand = new Random();
            i = Math.abs(rand.nextInt()) % locationsNumber;
            fireworkPosition = fireworks[i].getPosition();
            //select z directions
            boolean[] randFlag = new boolean[dimension];
            Arrays.fill(randFlag, false);

            int numExplosionDirections = (int) (dimension * Math.random());
            int randomCount = 0;
            int temporaryRandom;
            while (randomCount < numExplosionDirections) {
                temporaryRandom = Math.abs(rand.nextInt()) % dimension;
                if (!randFlag[temporaryRandom]) {
                    randFlag[temporaryRandom] = true;
                    randomCount++;
                }
            }

            //Gaussian Explosion (different explode)
            double gaussianCoefficient = GAUSSIAN_COEFFICIENT_BASE + rand.nextGaussian();
            for (j = 0; j < dimension; j++) {
                if (randFlag[j]) {
                    temporalPosition[j] = fireworkPosition[j] * gaussianCoefficient;
                    if (temporalPosition[j] < minimalBound[j] || temporalPosition[j] > maximumBound[j]) {
                        double abspos = Math.abs(temporalPosition[j]);
                        while (abspos >= 0) {
                            abspos -= (maximumBound[j] - minimalBound[j]);
                        }
                        abspos += (maximumBound[j] - minimalBound[j]);
                        temporalPosition[j] = minimalBound[j] + abspos;
                    }
                } else {
                    temporalPosition[j] = fireworkPosition[j];
                }
            }
            gaussianSparks[k].setPosition(temporalPosition);
        }
    }

    private double[] generateSparksForAllFireworks(int[] sparksNumber, double[] explosionAmplitude) {
        int i;
        double[] temporalPosition = new double[dimension], fireworkPosition;
        boolean [] randomFlag;
        Random random;
        int explosionDirectionsNumber, randomCount, temporaryRandom;
        for (i = 0; i < locationsNumber; i++) {
            sparks[i] = new Spark[sparksNumber[i]];
            fireworkPosition = fireworks[i].getPosition();
            for (int k = 0; k < sparksNumber[i]; k++) {
                sparks[i][k] = new Spark();
                //select z directions
                random = new Random();
                randomFlag = new boolean[dimension];
                Arrays.fill(randomFlag, false);
                explosionDirectionsNumber = (int) (dimension * Math.random());
                randomCount = 0;
                while (randomCount < explosionDirectionsNumber) {
                    temporaryRandom = random.nextInt(dimension);
                    if (!randomFlag[temporaryRandom]) {
                        randomFlag[temporaryRandom] = true;
                        randomCount++;
                    }
                }
                //explode
                explode(explosionAmplitude[i], temporalPosition, fireworkPosition, randomFlag);
                //set position of the spark
                sparks[i][k].setPosition(temporalPosition);
            }
        }
        return temporalPosition;
    }

    private void explode(double v, double[] temporalPosition, double[] fireworkPosition, boolean[] randomFlag) {
        int j;
        double displacementRatio = v * (Math.random() - HALF) * DOUBLE_COEFFICIENT;
        for (j = 0; j < dimension; j++) {
            if (randomFlag[j]) {
                temporalPosition[j] = fireworkPosition[j] + displacementRatio;
                if (temporalPosition[j] < minimalBound[j] || temporalPosition[j] > maximumBound[j]) {
                    double absolutePosition = Math.abs(temporalPosition[j]);
                    while (absolutePosition >= 0) {
                        absolutePosition -= (maximumBound[j] - minimalBound[j]);
                    }
                    absolutePosition += (maximumBound[j] - minimalBound[j]);
                    temporalPosition[j] = minimalBound[j] + absolutePosition;
                }
            } else {
                temporalPosition[j] = fireworkPosition[j];
            }
        }
    }

    private void getExplosionAmplitudeForAllFireworks(double minimumValue, double sumMinDiff, double[] explosionAmplitude) {
        int i;
        for (i = 0; i < locationsNumber; i++) {
            explosionAmplitude[i] = (fireworks[i].getValue(benchmarkFunction) - minimumValue + EPS_VALUE) / (sumMinDiff + EPS_VALUE) * maximumAmplitudeNumber;
        }
    }

    private void getNumberOfSparksForAllFireworks(double maximumValue, double sumMaxDiff, int[] sparksNumber) {
        int i;
        double temporalCoefficient;
        for (i = 0; i < locationsNumber; i++) {
            temporalCoefficient = (maximumValue - fireworks[i].getValue(benchmarkFunction) + EPS_VALUE) / (sumMaxDiff + EPS_VALUE);
            if (temporalCoefficient < lowBound) {
                temporalCoefficient = lowBound;
            }
            if (temporalCoefficient > highBound) {
                temporalCoefficient = highBound;
            }
            sparksNumber[i] = (int) (maximumSparksNumber * temporalCoefficient);
        }
    }

    private void selectNLocations() {
        Spark bestSpark = selectBestLocation();
        outputBestValue(bestSpark);
        final int fireworkSparksNumber = calculateNumberOfFireworksAndSparks();
        numFunctionEvaluations += fireworkSparksNumber;
        final double[][] fireworkAndSparksPositions = storeFireworksAndSparks(fireworkSparksNumber);
        final double[] cumulativeProbability = calculateProbabilityForEachLocation(fireworkSparksNumber, fireworkAndSparksPositions);
        final int[] nextLocations = selectLocationsDueToProbability(fireworkSparksNumber, cumulativeProbability);
        setNextGenerations(bestSpark, nextLocations);
    }

    private void setNextGenerations(final Spark bestSpark, final int[] nextLocations) {
        int i, j, k, index;
        boolean breakFlag;
        Spark[] nextFireworks = new Spark[locationsNumber];
        nextFireworks[locationsNumber - 1] = bestSpark;
        for (k = 0; k < locationsNumber - 1; k++) {
            index = 0;
            breakFlag = false;
            for (i = 0; i < locationsNumber; i++) {
                if (index == nextLocations[k]) {
                    nextFireworks[k] = fireworks[i];
                    breakFlag = true;
                    break;
                }
                index++;
            }
            if (breakFlag) {
                continue;
            }
            for (i = 0; i < locationsNumber; i++) {
                for (j = 0; j < sparks[i].length; j++) {
                    if (index == nextLocations[k]) {
                        nextFireworks[k] = sparks[i][j];
                        breakFlag = true;
                        break;
                    }
                    index++;
                }
                if (breakFlag) {
                    break;
                }
            }
            if (breakFlag) {
                continue;
            }

            for (i = 0; i < gaussianSparksNumber; i++) {
                if (index == nextLocations[k]) {
                    nextFireworks[k] = gaussianSparks[i];
                    break;
                }
                index++;
            }
        }
        fireworks = nextFireworks;
    }

    private int[] selectLocationsDueToProbability(final int fireworkSparksNumber, final double[] cumulativeProbability) {
        int i, k;
        double randomPointer;
        int[] nextLocations = new int[locationsNumber - 1];
        for (k = 0; k < locationsNumber - 1; k++) {
            randomPointer = Math.random();
            for (i = 0; i < fireworkSparksNumber; i++) {
                if (randomPointer <= cumulativeProbability[i]) {
                    break;
                }
            }
            nextLocations[k] = i;
        }
        return nextLocations;
    }

    private double[] calculateProbabilityForEachLocation(final int fireworkSparksNumber, final double[][] fireworkAndSparksPositions) {
        int i, j, k;
        double[] probabilityOfSelection = new double[fireworkSparksNumber];
        double sumOfProbability = 0, auxDistance;
        for (i = 0; i < fireworkSparksNumber; i++) {
            probabilityOfSelection[i] = 0;
            for (j = 0; j < fireworkSparksNumber; j++) {
                auxDistance = 0;
                for (k = 0; k < dimension; k++) {
                    auxDistance += (fireworkAndSparksPositions[i][k] - fireworkAndSparksPositions[j][k]) *
                            (fireworkAndSparksPositions[i][k] - fireworkAndSparksPositions[j][k]);
                }
                probabilityOfSelection[i] += Math.sqrt(auxDistance);
            }
            sumOfProbability += probabilityOfSelection[i];
        }

        double[] cumulativeProbability = new double[fireworkSparksNumber];
        for (i = 0; i < fireworkSparksNumber; i++) {
            if (sumOfProbability < EPS_VALUE) {
                probabilityOfSelection[i] = GAUSSIAN_COEFFICIENT_BASE / fireworkSparksNumber;
            } else {
                probabilityOfSelection[i] /= sumOfProbability;
            }
            cumulativeProbability[i] = (i == 0) ? probabilityOfSelection[i] : cumulativeProbability[i - 1] +
                    probabilityOfSelection[i];
        }
        return cumulativeProbability;
    }

    private double[][] storeFireworksAndSparks(int fireworkSparksNumber) {
        int i, j, index = 0;
        double[][] fireworkAndSparksPositions = new double[fireworkSparksNumber][];
        for (i = 0; i < locationsNumber; i++) {
            fireworkAndSparksPositions[index] = fireworks[i].getPosition();
            index++;
        }
        for (i = 0; i < locationsNumber; i++) {
            for (j = 0; j < sparks[i].length; j++) {
                fireworkAndSparksPositions[index] = sparks[i][j].getPosition();
                index++;
            }
        }
        for (i = 0; i < gaussianSparksNumber; i++) {
            fireworkAndSparksPositions[index] = gaussianSparks[i].getPosition();
            index++;
        }
        return fireworkAndSparksPositions;
    }

    private int calculateNumberOfFireworksAndSparks() {
        int i, j;
        int fireworkSparksNumber = locationsNumber + gaussianSparksNumber;
        for (i = 0; i < locationsNumber; i++) {
            for (j = 0; j < sparks[i].length; j++) {
                fireworkSparksNumber++;
            }
        }
        return fireworkSparksNumber;
    }

    private Spark selectBestLocation() {
        //select the best location
        Spark bestSpark = fireworks[0];
        int i, j;
        for (i = 1; i < locationsNumber; i++) {
            if (fireworks[i].getValue(benchmarkFunction) < bestSpark.getValue(benchmarkFunction)) {
                bestSpark = fireworks[i];
            }
        }
        for (i = 0; i < locationsNumber; i++) {
            for (j = 0; j < sparks[i].length; j++) {
                if (sparks[i][j].getValue(benchmarkFunction) < bestSpark.getValue(benchmarkFunction)) {
                    bestSpark = sparks[i][j];
                }
            }
        }
        for (i = 0; i < gaussianSparksNumber; i++) {
            if (gaussianSparks[i].getValue(benchmarkFunction) < bestSpark.getValue(benchmarkFunction)) {
                bestSpark = gaussianSparks[i];
            }
        }
        optimumValue = bestSpark.getValue(benchmarkFunction);
        return bestSpark;
    }

    private boolean stopCriteria() {
        //if(numGenerations < 2000) {
        boolean success;
        if (numFunctionEvaluations < NUMBER_OF_FUNCTION_EVALUATIONS) { //Readme: This const defines the iterations of the algorithm. 300000 - 400000 are reliable values
            success = false;
        } else {
            //System.out.println("numGenerations=" + numGenerations + ",numFunctionEvaluations=" + numFunctionEvaluations);
            success = true;
        }
        return success;
    }

    private void outputBestValue(final Spark bestSpark) {
        PrintStream printStream = null;
        final boolean append = true;
        try {
            printStream = new PrintStream(new FileOutputStream(infoFilePath, append));
        } catch (FileNotFoundException fnfe) {
            System.out.println(new Date() + OUTPUT_FILE_NOT_FOUND_IN_PATH + infoFilePath);
            fnfe.printStackTrace();
        }
        printStream.println(BEST_VALUE_FOUND + (1 - optimumValue));
//        printStream.println(BEST_POSITION_FOUND + bestSpark.getPosition().toString());
        printStream.println(BEST_POSITION_FOUND + Arrays.toString(bestSpark.getPosition()));
        printStream.println(SEPARATOR);
        printStream.close();
    }
}
