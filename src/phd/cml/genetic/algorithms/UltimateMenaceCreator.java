package phd.cml.genetic.algorithms;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Clemencio Morales Lucas on 24/11/2015.
 */
public class UltimateMenaceCreator {

    public static final int BIT_ONE = 1;
    public static final int BIT_ZERO = 0;

    public static final int GENOTYPE_LENGTH = 10;
    public static final int CHROMOSOME_LENGTH = 7;

    public static final int NUMBER_OF_ITERATIONS = 100;

    public enum Species {
        BUTTERFLY, PREDATOR, ALIEN, CAT, TERMINATOR, KOALA, HAKE
    }

    private int genotypeLength;
    private int chromosomeLength;
    private int best;
    private int numberOfIterations;
    private boolean[][] chromosome;

    public UltimateMenaceCreator(final int genotypeLength, final int chromosomeLength, final int numberOfIterations) {
        this.setGenotypeLength(genotypeLength);
        this.setChromosomeLength(chromosomeLength);
        this.setNumberOfIterations(numberOfIterations);
        this.initialize();
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

    private void initialize(){
        //AG Population
        this.setChromosome(new boolean[this.getGenotypeLength()][this.getChromosomeLength()]);

        //Random Genotipe Initialization
        for (int i = 0; i < this.getGenotypeLength(); i++) {
            for (int j = 0; j < this.getChromosomeLength(); j++) {
                chromosome[i][j] = getRandomBoolean();
            }
        }
    }

    public void evolve(final boolean verboseMode){
        best = 0; //Index of the best element among the population
        for (int g = 0; g < this.getNumberOfIterations(); g++) {
            if(verboseMode){
                System.out.println("Generation #" + g);
            }

            //Genotipe Evaluation
            for (int i = 1; i < this.getGenotypeLength(); i++) {
                if (fitnessFunction(chromosome[best]) < fitnessFunction(chromosome[i])) {
                    best = i;
                }
            }

            //Genetic operators application
            for (int i = 0; i < this.getGenotypeLength(); i++) {
                //Crossover that avoids self-reproduction
                if (i != best) {
                    for (int j = 0; j < this.getChromosomeLength(); j++){
                        if (getRandomBoolean()) {
                            chromosome[i][j] = chromosome[best][j];
                        } else {
                            chromosome[i][j] = chromosome[i][j];
                        }

                        //Mutation (lower probability)
                        if (new Random().nextInt(100) % 100 < 4) {
                            chromosome[i][j] = getRandomBoolean();
                        }
                    }
                }
            }
            if(verboseMode){
                printCurrentPhenotype(chromosome[best]);
            }
        }
    }

    public void printPhenotype(){
        System.out.println();
        System.out.println("************** PHENOTYPE FOUND **************");
        System.out.println("Best fitness: " + fitnessFunction(chromosome[best]));
        printBestPhenotype(chromosome[best]);
        System.out.println();
    }

    private static boolean getRandomBoolean() {
        return Math.random() < 0.5;
    }

    private static int fitnessFunction(final boolean[] chromosome) {
        final int[] intChromosome = parseToIntArray(chromosome);

        //Species available (We´d like to mix 1, 2 and 4 mainly):
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
        System.out.println("Fitness: " + fitnessFunction(chromosome));
        System.out.println(Arrays.toString(chromosome));
        System.out.println("----------------------------------------------");
    }

    private static void printBestPhenotype(final boolean[] chromosome){
        String result = "Ultimate menace formed by the mixture of:\n[";
        String discarded = "Discarded specimens:\n[";
        for(int i = 0; i < chromosome.length; i++) {
            if(chromosome[i]){
                result += Species.values()[i] + ",";
            } else {
                discarded += Species.values()[i] + ",";
            }
        }
        result = result.replaceAll(",$", "") + "]";
        discarded = discarded.replaceAll(",$", "") + "]";

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