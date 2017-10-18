package phd.cml.genetic.algorithms;

/**
 * Created by Clemencio Morales Lucas.
 */

interface UltimateMenaceCreatorConstants {

    int BIT_ONE = 1;
    int BIT_ZERO = 0;
    int GENOTYPE_LENGTH = 10;
    int CHROMOSOME_LENGTH = 7;
    int NUMBER_OF_ITERATIONS = 100;
    String GENERATION_NUMBER_MESSAGE = "Generation #";
    int MUTATION_PROBABILITY = 4;
    int PERCENTAGE_LIMIT = 100;
    String PHENOTYPE_FOUND_MESSAGE = "************** PHENOTYPE FOUND **************";
    String BEST_FITNESS_MESSAGE = "Best fitness: ";
    double HALF_DOUBLE = 0.5;
    String FITNESS_MESSAGE = "Fitness: ";
    String ULTIMATE_MENACE_FORMED_BY_THE_MIXTURE_OF_MESSAGE = "Ultimate menace formed by the mixture of:\n[";
    String DISCARDED_SPECIMENS_MESSAGE = "Discarded specimens:\n[";
    String COMMA_CHARACTER = ",";
    String EMPTY_STRING = "";
    String SPECIAL_REGEX = ",$";
    String CLOSING_BRACKET = "]";
    enum Species { BUTTERFLY, PREDATOR, ALIEN, CAT, TERMINATOR, KOALA, HAKE }
}
