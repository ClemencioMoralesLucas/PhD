package phd.cml.fireworks;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Clemencio Morales Lucas.
 */

public class ImprovedFireworkAlgorithm implements FireworkAlgorithmConstants {

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
    private double optimumValue;
    private int numFunctionEvaluations;
    private BenchmarkFunction benchmarkFunction;

    public ImprovedFireworkAlgorithm(final int locationsNumber, final int maximumSparksNumber, final double lowBound,
                                     final double highBound, final double maximumAmplitudeNumber, final int gaussianSparksNumber,
                                     final double[] maximumBound, final double[] minimalBound, final String infoFilePath,
                                     final BenchmarkFunction benchmarkFunction) throws FileNotFoundException {
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

    private void setOffNFireworks() {
        int i;
        double maximumValue = fireworks[0].getValue(benchmarkFunction);
        double minimumValue = fireworks[0].getValue(benchmarkFunction);
        for (i = 1; i < this.locationsNumber; i++) {
            if (this.fireworks[i].getValue(this.benchmarkFunction) > maximumValue) {
                maximumValue = this.fireworks[i].getValue(this.benchmarkFunction);
            }
            if (this.fireworks[i].getValue(this.benchmarkFunction) < minimumValue) {
                minimumValue = this.fireworks[i].getValue(this.benchmarkFunction);
            }
        }
        double sumMaxDiff = 0.0;
        double sumMinDiff = 0.0;
        for (i = 0; i < locationsNumber; i++) {
            sumMaxDiff += maximumValue - fireworks[i].getValue(benchmarkFunction);
            sumMinDiff += fireworks[i].getValue(benchmarkFunction) - minimumValue;
        }
        explodeFireworks(maximumValue, minimumValue, sumMaxDiff, sumMinDiff);
    }

    private void selectNInitialLocations() {
        numFunctionEvaluations = 0;
        fireworks = new Spark[locationsNumber];
        double[] randomPosition = new double[dimension];

        for (int i = 0; i < this.locationsNumber; i++) {
            this.fireworks[i] = new Spark();
            for (int j = 0; j < this.dimension; j++) {
                randomPosition[j] = this.maximumBound[j] - Math.random() * (this.maximumBound[j] - this.minimalBound[j]) * QUARTER;
            }
            this.fireworks[i].setPosition(randomPosition);
        }
    }

    public double launch() throws FileNotFoundException {
        selectNInitialLocations();
        while (!stopCriteria()) {
            setOffNFireworks();
            selectNLocations();
        }
        return optimumValue;
    }

    private void explodeFireworks(final double maximumValue, final double minimumValue, final double sumMaxDiff, final double sumMinDiff) {
        final int[] sparksNumber = new int[locationsNumber];
        getNumberOfSparksForAllFireworks(maximumValue, sumMaxDiff, sparksNumber);
        final double explosionAmplitude[] = new double[locationsNumber];
        getExplosionAmplitudeForAllFireworks(minimumValue, sumMinDiff, explosionAmplitude);
        initializeAllSparks(sparksNumber, explosionAmplitude);
    }

    private void explode(final double customCoefficient, double[] temporalPosition, final double[] fireworkPosition,
                         final boolean[] randomFlag) {
        double displacementRatio = customCoefficient * (Math.random() - HALF) * DOUBLE_COEFFICIENT;
        for (int j = 0; j < dimension; j++) {
            updatePositionsIfValidCriteria(temporalPosition, fireworkPosition, randomFlag[j], displacementRatio, j);
        }
    }

    private void gaussianExplode(double[] temporalPosition) {
        for (int k = 0; k < gaussianSparksNumber; k++) {
            executeExplosion(temporalPosition, k);
        }
    }

    private void setNextGenerations(final Spark bestSpark, final int[] nextLocations) {
        int i, j, k, index;
        boolean breakFlag;
        Spark[] nextFireworks = new Spark[this.locationsNumber];
        nextFireworks[this.locationsNumber - 1] = bestSpark;
        for (k = 0; k < this.locationsNumber - 1; k++) {
            index = 0;
            breakFlag = false;
            for (i = 0; i < this.locationsNumber; i++) {
                if (index == nextLocations[k]) {
                    nextFireworks[k] = this.fireworks[i];
                    breakFlag = true;
                    break;
                }
                index++;
            }
            if (!breakFlag) {
                for (i = 0; i < this.locationsNumber; i++) {
                    for (j = 0; j < sparks[i].length; j++) {
                        if (index == nextLocations[k]) {
                            nextFireworks[k] = this.sparks[i][j];
                            breakFlag = true;
                            break;
                        }
                        index++;
                    }
                    if (breakFlag) break;
                }
                if (breakFlag) continue;

                for (i = 0; i < this.gaussianSparksNumber; i++) {
                    if (index == nextLocations[k]) {
                        nextFireworks[k] = this.gaussianSparks[i];
                        break;
                    }
                    index++;
                }
            }
        }
        this.fireworks = nextFireworks;
    }

    private void initializeAllSparks(int[] sparksNumber, double[] explosionAmplitude) {
        this.sparks = new Spark[locationsNumber][];
        final double[] temporalPosition = generateSparksForAllFireworks(sparksNumber, explosionAmplitude);
        this.gaussianSparks = new Spark[gaussianSparksNumber];
        gaussianExplode(temporalPosition);
    }

    private void createExplodeAndUpdateSparks(double v, int i, double[] temporalPosition, double[] fireworkPosition, int k) {
        Random random;
        boolean[] randomFlag;
        int explosionDirectionsNumber;
        int randomCount;
        sparks[i][k] = new Spark();
        random = new Random();
        randomFlag = new boolean[dimension];
        Arrays.fill(randomFlag, false);
        explosionDirectionsNumber = new Random().nextInt(dimension);
        randomCount = 0;
        while (randomCount < explosionDirectionsNumber) {
            randomCount = assertRandomFlag(random, randomFlag, randomCount);
        }
        explode(v, temporalPosition, fireworkPosition, randomFlag);
        sparks[i][k].setPosition(temporalPosition);
    }

    private int[] selectLocationsDueToProbability(final int fireworkSparksNumber, final double[] cumulativeProbability) {
        int i, k;
        double randomPointer;
        int[] nextLocations = new int[this.locationsNumber - 1];
        for (k = 0; k < this.locationsNumber - 1; k++) {
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

    private void calculateTemporaryFlaggedRandoms(final Random rand, final boolean[] randFlag) {
        int numExplosionDirections = new Random().nextInt(this.dimension), randomCount = 0, temporaryRandom;
        while (randomCount < numExplosionDirections) {
            temporaryRandom = Math.abs(rand.nextInt()) % this.dimension;
            if (!randFlag[temporaryRandom]) {
                randFlag[temporaryRandom] = true;
                randomCount++;
            }
        }
    }

    private int assertRandomFlag(Random random, boolean[] randomFlag, int randomCount) {
        int temporaryRandom;
        temporaryRandom = random.nextInt(dimension);
        if (!randomFlag[temporaryRandom]) {
            randomFlag[temporaryRandom] = true;
            randomCount++;
        }
        return randomCount;
    }

    private void updatePositionsIfValidCriteria(double[] tempPosition, double[] fireworkPosition, boolean continueFlag, double displacementRatio, int j) {
        if (continueFlag) {
            tempPosition[j] = fireworkPosition[j] + displacementRatio;
            if (tempPosition[j] < minimalBound[j] || tempPosition[j] > maximumBound[j]) {
                performPositionUpdate(tempPosition, j);
            }
        } else {
            tempPosition[j] = fireworkPosition[j];
        }
    }

    private double[] calculateProbabilityForEachLocation(final int fireworkSparksNumber, final double[][] fireworkAndSparksPositions) {
        int i, j, k;
        double[] selectionProbability = new double[fireworkSparksNumber];
        double accumulatedProbability = 0, auxDistance;
        for (i = 0; i < fireworkSparksNumber; i++) {
            selectionProbability[i] = 0;
            for (j = 0; j < fireworkSparksNumber; j++) {
                auxDistance = 0;
                for (k = 0; k < this.dimension; k++) {
                    auxDistance += (fireworkAndSparksPositions[i][k] - fireworkAndSparksPositions[j][k]) *
                            (fireworkAndSparksPositions[i][k] - fireworkAndSparksPositions[j][k]);
                }
                selectionProbability[i] += Math.sqrt(auxDistance);
            }
            accumulatedProbability += selectionProbability[i];
        }

        double[] cumulativeProbability = new double[fireworkSparksNumber];
        for (i = 0; i < fireworkSparksNumber; i++) {
            selectionProbability[i] = accumulatedProbability < EPS_VALUE ? COEFFICIENT_BASE / fireworkSparksNumber :
                    selectionProbability[i] / accumulatedProbability;
            cumulativeProbability[i] = (i == 0) ? selectionProbability[i] : cumulativeProbability[i - 1] +
                    selectionProbability[i];
        }
        return cumulativeProbability;
    }

    private double[] generateSparksForAllFireworks(final int[] sparksNumber, final double[] explosionAmplitude) {
        int i;
        double[] temporalPosition = new double[dimension], fireworkPosition;
        for (i = 0; i < locationsNumber; i++) {
            sparks[i] = new Spark[sparksNumber[i]];
            fireworkPosition = fireworks[i].getPosition();
            for (int k = 0; k < sparksNumber[i]; k++) {
                createExplodeAndUpdateSparks(explosionAmplitude[i], i, temporalPosition, fireworkPosition, k);
            }
        }
        return temporalPosition;
    }

    private void executeExplosion(final double[] temporalPosition, final int k) {
        Random rand;
        int i;
        double[] fireworkPosition;
        gaussianSparks[k] = new Spark();
        rand = new Random();
        i = Math.abs(rand.nextInt()) % locationsNumber;
        fireworkPosition = fireworks[i].getPosition();
        final boolean[] randFlag = new boolean[dimension];
        Arrays.fill(randFlag, false);
        calculateTemporaryFlaggedRandoms(rand, randFlag);
        performGaussianExplosion(temporalPosition, fireworkPosition, rand, randFlag);
        gaussianSparks[k].setPosition(temporalPosition);
    }

    private void performPositionUpdate(double[] tempPosition, final int positionIndex) {
        double absolutePosition = Math.abs(tempPosition[positionIndex]);
        while (absolutePosition >= 0) {
            absolutePosition -= (maximumBound[positionIndex] - minimalBound[positionIndex]);
        }
        absolutePosition += (maximumBound[positionIndex] - minimalBound[positionIndex]);
        tempPosition[positionIndex] = minimalBound[positionIndex] + absolutePosition;
    }

    private void getExplosionAmplitudeForAllFireworks(final double minimumValue, final double sumMinDiff,
                                                      double[] explosionAmplitude) {
        for (int i = 0; i < locationsNumber; i++) {
            explosionAmplitude[i] = (fireworks[i].getValue(benchmarkFunction) - minimumValue + EPS_VALUE) / (sumMinDiff + EPS_VALUE) * maximumAmplitudeNumber;
        }
    }

    private void getNumberOfSparksForAllFireworks(double maximumValue, double sumMaxDiff, int[] sparksNumber) {
        int i;
        double tempCoefficient;
        for (i = 0; i < this.locationsNumber; i++) {
            tempCoefficient = (maximumValue - fireworks[i].getValue(benchmarkFunction) + EPS_VALUE) / (sumMaxDiff + EPS_VALUE);
            if (tempCoefficient < lowBound) tempCoefficient = lowBound;
            if (tempCoefficient > highBound) tempCoefficient = highBound;
            sparksNumber[i] = (int) (maximumSparksNumber * tempCoefficient);
        }
    }


    private void performGaussianExplosion(final double[] temporalPosition, final double[] fireworkPosition,
                                          final Random rand, boolean[] randFlag) {
        final double gaussianCoefficient = COEFFICIENT_BASE + rand.nextGaussian();
        for (int j = 0; j < this.dimension; j++) {
            if (randFlag[j]) {
                temporalPosition[j] = fireworkPosition[j] * gaussianCoefficient;
                if (temporalPosition[j] < this.minimalBound[j] || temporalPosition[j] > this.maximumBound[j]) {
                    double absolutePosition = Math.abs(temporalPosition[j]);
                    while (absolutePosition >= 0) {
                        absolutePosition -= (this.maximumBound[j] - this.minimalBound[j]);
                    }
                    absolutePosition += (this.maximumBound[j] - this.minimalBound[j]);
                    temporalPosition[j] = this.minimalBound[j] + absolutePosition;
                }
            } else {
                temporalPosition[j] = fireworkPosition[j];
            }
        }
    }

    private void selectNLocations() throws FileNotFoundException {
        final Spark bestSpark = selectBestLocationDivideAndConquer();
        outputBestValue(bestSpark);
        final int fireworkSparksNumber = calculateNumberOfFireworksAndSparks();
        this.numFunctionEvaluations += fireworkSparksNumber;
        final int[] nextLocations = selectLocationsDueToProbability(fireworkSparksNumber,
                calculateProbabilityForEachLocation(fireworkSparksNumber, storeFireworksAndSparks(fireworkSparksNumber)));
        setNextGenerations(bestSpark, nextLocations);
    }

    private Spark calculateSparkFitnessByBenchmark(Spark bestSpark) {
        for (int i = 0; i < this.locationsNumber; i++) {
            for (int j = 0; j < this.sparks[i].length; j++) {
                if (this.sparks[i][j].getValue(this.benchmarkFunction) < bestSpark.getValue(this.benchmarkFunction)) {
                    bestSpark = this.sparks[i][j];
                }
            }
        }
        return bestSpark;
    }

    private int calculateNumberOfFireworksAndSparks() {
        int fireworkSparksNumber = this.locationsNumber + this.gaussianSparksNumber;
        for (int i = 0; i < this.locationsNumber; i++) {
            for (int j = 0; j < this.sparks[i].length; j++) {
                fireworkSparksNumber++;
            }
        }
        return fireworkSparksNumber;
    }

    private Spark applyGaussianUpdate(Spark bestSpark) {
        for (int i = 0; i < this.gaussianSparksNumber; i++) {
            if (this.gaussianSparks[i].getValue(this.benchmarkFunction) < bestSpark.getValue(this.benchmarkFunction)) {
                bestSpark = this.gaussianSparks[i];
            }
        }
        return bestSpark;
    }

    private Spark updateBestSparkIndex(Spark bestSpark) {
        for (int i = 1; i < this.locationsNumber; i++) {
            if (this.fireworks[i].getValue(this.benchmarkFunction) < bestSpark.getValue(this.benchmarkFunction)) {
                bestSpark = this.fireworks[i];
            }
        }
        return bestSpark;
    }

    private double[][] storeFireworksAndSparks(final int fireworkSparksNumber) {
        int i, j, index = 0;
        final double[][] fwAndSparksPositions = new double[fireworkSparksNumber][];
        for (i = 0; i < this.locationsNumber; i++) {
            fwAndSparksPositions[index] = this.fireworks[i].getPosition();
            index++;
        }
        for (i = 0; i < this.locationsNumber; i++) {
            for (j = 0; j < this.sparks[i].length; j++) {
                fwAndSparksPositions[index] = this.sparks[i][j].getPosition();
                index++;
            }
        }
        for (i = 0; i < this.gaussianSparksNumber; i++) {
            fwAndSparksPositions[index] = this.gaussianSparks[i].getPosition();
            index++;
        }
        return fwAndSparksPositions;
    }

    private Spark selectBestLocationDivideAndConquer() {
        Spark bestSpark = this.fireworks[0];
        bestSpark = updateBestSparkIndex(bestSpark);
        bestSpark = calculateSparkFitnessByBenchmark(bestSpark);
        bestSpark = applyGaussianUpdate(bestSpark);
        this.optimumValue = bestSpark.getValue(this.benchmarkFunction);
        return bestSpark;
    }

    private boolean stopCriteria() {
        return numFunctionEvaluations >= NUMBER_OF_FUNCTION_EVALUATIONS;
    }

    private void outputBestValue(final Spark bestSpark) throws FileNotFoundException {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LINE_BREAK + ImprovedFireworkAlgorithm.BEST_VALUE_FOUND + (1 - this.optimumValue));
        stringBuilder.append(LINE_BREAK + ImprovedFireworkAlgorithm.BEST_POSITION_FOUND + Arrays.toString(bestSpark.getPosition()));
        stringBuilder.append(LINE_BREAK + ImprovedFireworkAlgorithm.SEPARATOR);
        System.out.println(stringBuilder.toString());
    }
}
