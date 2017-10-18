package phd.cml.genetic.algorithms;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Clemencio Morales Lucas.
 */

public class UltimateMenaceCreator implements UltimateMenaceCreatorConstants {

    private int genotypeLength;
    private int chromosomeLength;
    private int best;
    private int numberOfIterations;
    private boolean[][] chromosome;

    public UltimateMenaceCreator(final int genotypeLength, final int chromosomeLength, final int numberOfIterations) {
        this.setGenotypeLength(genotypeLength);
        this.setChromosomeLength(chromosomeLength);
        this.setNumberOfIterations(numberOfIterations);
        this.initializeChromosomicPopulation();
    }

    public int getGenotypeLength() {
        return genotypeLength;
    }

    public void setGenotypeLength(final int genotypeLength) {
        this.genotypeLength = genotypeLength;
    }

    public int getChromosomeLength() {
        return chromosomeLength;
    }

    public void setChromosomeLength(final int chromosomeLength) {
        this.chromosomeLength = chromosomeLength;
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public int getBest() {
        return best;
    }

    public void setBest(int best) {
        this.best = best;
    }

    public boolean[][] getChromosome() {
        return chromosome;
    }

    public void setChromosome(final boolean[][] chromosome) {
        this.chromosome = chromosome;
    }

    private void initializeChromosomicPopulation(){
        this.setChromosome(new boolean[this.getGenotypeLength()][this.getChromosomeLength()]);
        for (int i = 0; i < this.getGenotypeLength(); i++) {
            for (int j = 0; j < this.getChromosomeLength(); j++) {
                chromosome[i][j] = getRandomBoolean();
            }
        }
    }

    public void evolve(final boolean verboseMode){
        best = 0;
        for (int g = 0; g < this.getNumberOfIterations(); g++) {
            if(verboseMode){
                System.out.println(GENERATION_NUMBER_MESSAGE + g);
            }
            updateCurrentBestGenotipe();
            applyGeneticOperators();

            if(verboseMode){
                printCurrentPhenotype(chromosome[best]);
            }
        }
    }

    private void applyGeneticOperators() {
        for (int i = 0; i < this.getGenotypeLength(); i++) {
            if (i != best) {  //Crossover that avoids self-reproduction
                for (int j = 0; j < this.getChromosomeLength(); j++){
                    if (getRandomBoolean()) {
                        chromosome[i][j] = chromosome[best][j];
                    } else {
                        chromosome[i][j] = chromosome[i][j];
                    }
                    performLowProbabilityMutation(i, j);
                }
            }
        }
    }

    private void performLowProbabilityMutation(final int x, final int y) {
        if (new Random().nextInt(PERCENTAGE_LIMIT) % PERCENTAGE_LIMIT < MUTATION_PROBABILITY) {
            chromosome[x][y] = getRandomBoolean();
        }
    }

    private void updateCurrentBestGenotipe() {
        for (int i = 1; i < this.getGenotypeLength(); i++) {
            if (fitnessFunction(chromosome[best]) < fitnessFunction(chromosome[i])) {
                best = i;
            }
        }
    }

    public void printPhenotype(){
        System.out.println();
        System.out.println(PHENOTYPE_FOUND_MESSAGE);
        System.out.println(BEST_FITNESS_MESSAGE + fitnessFunction(chromosome[best]));
        printBestPhenotype(chromosome[best]);
        System.out.println();
    }

    private static boolean getRandomBoolean() {
        return Math.random() < HALF_DOUBLE;
    }

    private static int fitnessFunction(final boolean[] chromosome) {
        final int[] intChromosome = parseToIntArray(chromosome);
        //Species available (We'd like to mix 1, 2 and 4 mainly):
        // 0            1        2     3        4        5     6
        //BUTTERFLY, PREDATOR, ALIEN, CAT, TERMINATOR, KOALA, HAKE
        return (-intChromosome [0] + intChromosome [1] + intChromosome [2]
                -intChromosome [3] + intChromosome [4] - intChromosome [5]
                -intChromosome [6] );
    }

    private static int[] parseToIntArray(final boolean[] booleanArray) {
        final int[] intArray = new int[booleanArray.length];
        for (int i = 0; i < booleanArray.length; i++) {
            intArray[i] = booleanArray[i] ? BIT_ONE : BIT_ZERO;
        }
        return intArray;
    }

    private static void printCurrentPhenotype(boolean[] chromosome) {
        System.out.println(FITNESS_MESSAGE + fitnessFunction(chromosome));
        System.out.println(Arrays.toString(chromosome));
        System.out.println("----------------------------------------------");
    }

    private static void printBestPhenotype(final boolean[] chromosome){
        String result = ULTIMATE_MENACE_FORMED_BY_THE_MIXTURE_OF_MESSAGE;
        String discarded = DISCARDED_SPECIMENS_MESSAGE;
        for(int i = 0; i < chromosome.length; i++) {
            if(chromosome[i]){
                result += Species.values()[i] + COMMA_CHARACTER;
            } else {
                discarded += Species.values()[i] + COMMA_CHARACTER;
            }
        }
        result = result.replaceAll(SPECIAL_REGEX, EMPTY_STRING) + CLOSING_BRACKET;
        discarded = discarded.replaceAll(SPECIAL_REGEX, EMPTY_STRING) + CLOSING_BRACKET;

        System.out.println(result);
        System.out.println();
        System.out.println(discarded);
    }

    public static void main(String [] args) {
        UltimateMenaceCreator ultimateMenaceCreator =
                new UltimateMenaceCreator(GENOTYPE_LENGTH, CHROMOSOME_LENGTH, NUMBER_OF_ITERATIONS);
        final boolean verboseMode = true;
        ultimateMenaceCreator.evolve(verboseMode);
        ultimateMenaceCreator.printPhenotype();
    }
}
