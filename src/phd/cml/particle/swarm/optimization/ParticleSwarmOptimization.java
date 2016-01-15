package phd.cml.particle.swarm.optimization;

/**
 * Created by Clemencio Morales Lucas on 14/12/2015.
 */

import java.util.Vector;
import java.util.Random;

public class ParticleSwarmOptimization {

    // With a smaller tolerance, the result is more accurate, but iteration number increases
    public static final double ERR_TOLERANCE = 1E-20;

    private static final int SWARM_SIZE = 30;
    private static final int MAX_ITERATION = 100;
    private static final int PROBLEM_DIMENSION = 2;

    private static final double C1 = 2.0;
    private static final double C2 = 2.0;

    private static final double W_UPPER_BOUND = 1.0;
    private static final double W_LOWER_BOUND = 0.0;

    private Vector<Particle> swarm = new Vector<>();

    private double[] pBest = new double[SWARM_SIZE];
    private Vector<Location> pBestLocation = new Vector<>();

    private double gBest;
    private Location gBestLocation;

    private double[] fitnessValueList = new double[SWARM_SIZE];

    private Random random = new Random();

    public void execute(final boolean verboseMode) {
        int iteration = 0;
        double w;
        double error = 9999;

        this.initializeSwarm();
        this.updateFitnessList();

        for(int i=0; i < SWARM_SIZE; i++) {
            pBest[i] = fitnessValueList[i];
            pBestLocation.add((Location) swarm.get(i).getLocation());
        }

        while(iteration < MAX_ITERATION && error > ERR_TOLERANCE) {
            // First action -> Update pBest
            for(int i=0; i < SWARM_SIZE; i++) {
                if(fitnessValueList[i] < pBest[i]) {
                    pBest[i] = fitnessValueList[i];
                    pBestLocation.set(i, (Location) swarm.get(i).getLocation());
                }
            }

            // Second action - Update gBest
            int bestParticleIndex = getMinimumPosition(fitnessValueList);
            if(iteration == 0 || fitnessValueList[bestParticleIndex] < gBest) {
                gBest = fitnessValueList[bestParticleIndex];
                gBestLocation = (Location) swarm.get(bestParticleIndex).getLocation();
            }

            w = W_UPPER_BOUND - (((double) iteration) / MAX_ITERATION) * (W_UPPER_BOUND - W_LOWER_BOUND);

            for(int i=0; i<SWARM_SIZE; i++) {
                double r1 = random.nextDouble();
                double r2 = random.nextDouble();
                Particle p = swarm.get(i);

                // Third step - Update velocity
                double[] newVelocity = new double[PROBLEM_DIMENSION];

                newVelocity[0] = (w * p.getVelocity().getValues()[0]) +
                        (r1 * C1) * (pBestLocation.get(i).getValues()[0] - p.getLocation().getValues()[0]) +
                        (r2 * C2) * (gBestLocation.getValues()[0] - p.getLocation().getValues()[0]);

                newVelocity[1] = (w * p.getVelocity().getValues()[1]) +
                        (r1 * C1) * (pBestLocation.get(i).getValues()[1] - p.getLocation().getValues()[1]) +
                        (r2 * C2) * (gBestLocation.getValues()[1] - p.getLocation().getValues()[1]);

                Velocity vel = new Velocity(newVelocity);
                p.setVelocity(vel);

                // Fourth step - Update location
                double[] newLocation = new double[PROBLEM_DIMENSION];

                newLocation[0] = p.getLocation().getValues()[0] + newVelocity[0];
                newLocation[1] = p.getLocation().getValues()[1] + newVelocity[1];

                Location loc = new Location(newLocation);
                p.setLocation(loc);
            }

            //Observation: The function ought to be minimized, thus it means it´s getting closer to 0
            error = Particle.evaluate(gBestLocation) - 0;

            if(verboseMode){
                printCurrentResults(iteration);
            }

            iteration++;
            updateFitnessList();
        }

        printFinalResults(iteration-1);
    }

    public void initializeSwarm() {
        Particle p;
        for(int i=0; i<SWARM_SIZE; i++) {
            p = new Particle();

            // Random location initialization within the scenario space
            double[] loc = new double[PROBLEM_DIMENSION];

            loc[0] = Particle.LOCATION_X_LOW + random.nextDouble() * (Particle.LOCATION_X_HIGH - Particle.LOCATION_X_LOW);
            loc[1] = Particle.LOCATION_Y_LOW + random.nextDouble() * (Particle.LOCATION_Y_HIGH - Particle.LOCATION_Y_LOW);
            PhysicalMagnitude location = new Location(loc);

            // Random velocity initialization within the scenario range
            double[] vel = new double[PROBLEM_DIMENSION];

            vel[0] = Particle.VELOCITY_LOW + random.nextDouble() * (Particle.VELOCITY_HIGH - Particle.VELOCITY_LOW);
            vel[1] = Particle.VELOCITY_LOW + random.nextDouble() * (Particle.VELOCITY_HIGH - Particle.VELOCITY_LOW);
            PhysicalMagnitude velocity = new Velocity(vel);

            p.setLocation(location);
            p.setVelocity(velocity);
            swarm.add(p);
        }
    }

    public void updateFitnessList() {
        for(int i=0; i < SWARM_SIZE; i++) {
            fitnessValueList[i] = swarm.get(i).getFitnessValue();
        }
    }

    private void printCurrentResults(final int iterationNumber) {
        System.out.println();
        System.out.println("Iteration #" + iterationNumber + ": ");
        System.out.println("  --> Best X value: " + gBestLocation.getValues()[0]);
        System.out.println("  --> Best Y value: " + gBestLocation.getValues()[1]);
        System.out.println("  --> Evaluation value: " + Particle.evaluate(gBestLocation));
    }

    private void printFinalResults(final int iterationNumber){
        System.out.println();
        System.out.println("-------------------------------------------------");
        System.out.println("Solution found at Iteration #" + (iterationNumber) + ". Values:");
        System.out.println("  ====> Best X: " + gBestLocation.getValues()[0]);
        System.out.println("  ====> Best Y: " + gBestLocation.getValues()[1]);
    }

    public static int getMinimumPosition(double[] list) {
        int pos = 0;
        double minValue = list[0];

        for(int i=0; i < list.length; i++) {
            if(list[i] < minValue) {
                pos = i;
                minValue = list[i];
            }
        }
        return pos;
    }
}
