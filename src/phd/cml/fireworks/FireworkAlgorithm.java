package phd.cml.fireworks;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;
/**
 * Created by Clemencio Morales Lucas.
 */

//TODO: Refactor all and then extract every single snippet of code under comments like "explode" to a method called like that (give a go to the whole code)
public class FireworkAlgorithm {

    private static final double EPS_VALUE = 1e-38;
    private static final double QUARTER = 0.25;
    private static final double HALF = 0.5;
    private static final int DOUBLE_COEFFICIENT = 2;
    private static final double GAUSSIAN_COEFFICIENT_BASE = 1.0;
    private static final String BEST_VALUE_FOUND = "Best value found ==> ";
    private static final String BEST_POSITION_FOUND = "best position found ==> ";
    private static final String SEPARATOR = "---------------------------------------------------------";

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

    public FireworkAlgorithm(final int locationsNumber, final int maximumSparksNumber, final double lowBound,
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

    public double fireworksAlgorithmFramework() {
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

        //get number of sparks for all fireworks
        int[] sparksNumber = new int[locationsNumber];
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

        //get amplitude of explosion for all fireworks

        double explosionAmplitude[] = new double[locationsNumber];
        for (i = 0; i < locationsNumber; i++) {
            explosionAmplitude[i] = (fireworks[i].getValue(benchmarkFunction) - minimumValue + EPS_VALUE) / (sumMinDiff + EPS_VALUE) * maximumAmplitudeNumber;
        }

        //generate sparks for all fireworks

        sparks = new Spark[locationsNumber][];
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
                int j;
                for (j = 0; j < dimension; j++) {
                    randomFlag[j] = false;
                }
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
                double displacementRatio = explosionAmplitude[i] * (Math.random() - HALF) * DOUBLE_COEFFICIENT;
                for (j = 0; j < dimension; j++) {
                    if (randomFlag[j]) {
                        temporalPosition[j] = fireworkPosition[j] + displacementRatio;
                        //out of bound
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
                //set position of the spark
                sparks[i][k].setPosition(temporalPosition);
            }
        }

        //TODO Snippet to refactor as long as it has code which is almost a duplication
        //gaussian explode
        gaussianSparks = new Spark[gaussianSparksNumber];
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
            for (j = 0; j < dimension; j++) {
                randFlag[j] = false;
            }
            int numExplosionDirections = (int) (dimension * Math.random());
            int randomcount = 0;
            int tmprand;
            while (randomcount < numExplosionDirections) {
                tmprand = Math.abs(rand.nextInt()) % dimension;
                if (!randFlag[tmprand]) {
                    randFlag[tmprand] = true;
                    randomcount++;
                }
            }

            //TODO Snippet to refactor as long as it has code which is almost a duplication (is similar to the last explode and could be refactored)
            //explode
            double gaussianCoefficient = GAUSSIAN_COEFFICIENT_BASE + rand.nextGaussian();
            for (j = 0; j < dimension; j++) {
                if (randFlag[j]) {
                    temporalPosition[j] = fireworkPosition[j] * gaussianCoefficient;
                    //out of bound
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
            //set position of the spark
            gaussianSparks[k].setPosition(temporalPosition);
        }
    }

    //select n locations
    private void selectNLocations() {
        //select the best location
        Spark bestSpark = fireworks[0];
        int i, j, k;
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

        //output the best value
        PrintStream printStream = null;
        try {
            printStream = new PrintStream(new FileOutputStream(infoFilePath, true));
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        printStream.println(BEST_VALUE_FOUND + (1 - optimumValue));
//        printStream.println(BEST_POSITION_FOUND + bestSpark.getPosition().toString());
        printStream.println(BEST_POSITION_FOUND + Arrays.toString(bestSpark.getPosition()));
        printStream.println(SEPARATOR);
        printStream.close();

        //select the rest n-1 locations
        //count the number of fireworks and sparks
        int fireworkSparksNumber = locationsNumber + gaussianSparksNumber;
        for (i = 0; i < locationsNumber; i++) {
            for (j = 0; j < sparks[i].length; j++) {
                fireworkSparksNumber++;
            }
        }

        //calculate the number of function evaluations
        numFunctionEvaluations += fireworkSparksNumber;

        //put all the fireworks and sparks in an array
        double[][] fireworkAndSparksPositions = new double[fireworkSparksNumber][];
        int index = 0;
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

        //calculate the selection probability of each location
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
        //select n-1 locations according to the selection probability
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

        //set next generations
        Spark[] nextFireworks = new Spark[locationsNumber];
        nextFireworks[locationsNumber - 1] = bestSpark;
        boolean breakFlag;
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

    private boolean stopCriteria() {
        //if(numGenerations < 2000) {
//		if(numFunctionEvaluations < 300000) {
        if (numFunctionEvaluations < 30) {
            return false;
        } else {
            //System.out.println("numGenerations=" + numGenerations + ",numFunctionEvaluations=" + numFunctionEvaluations);
            return true;
        }
    }
}
