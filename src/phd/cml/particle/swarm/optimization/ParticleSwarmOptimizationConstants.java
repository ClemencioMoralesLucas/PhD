package phd.cml.particle.swarm.optimization;

/**
 * Created by Clemencio Morales Lucas.
 */
public interface ParticleSwarmOptimizationConstants {

    // With a smaller tolerance, the result is more accurate, but iteration number increases
    double ERR_TOLERANCE = 1E-20;
    int SWARM_SIZE = 30;
    int MAX_ITERATION = 100;
    int PROBLEM_DIMENSION = 2;
    double C1 = 2.0;
    double C2 = 2.0;
    double W_UPPER_BOUND = 1.0;
    double W_LOWER_BOUND = 0.0;
    String ITERATION_NUMBER_MESSAGE = "Iteration #";
    String COLON_SEPARATOR = ": ";
    String BEST_X_VALUE_MESSAGE = "  --> Best X value: ";
    String BEST_Y_VALUE_MESSAGE = "  --> Best Y value: ";
    String EVALUATION_VALUE_MESSAGE = "  --> Evaluation value: ";
    String SEPARATOR = "-------------------------------------------------";
    String SOLUTION_FOUND_AT_ITERATION_MESSAGE = "Solution found at Iteration #";
    String VALUES_MESSAGE = ". Values:";
    String BEST_X_MESSAGE = "  ====> Best X: ";
    String BEST_Y_MESSAGE = "  ====> Best Y: ";
}
