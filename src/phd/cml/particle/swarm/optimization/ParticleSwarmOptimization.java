package phd.cml.particle.swarm.optimization;

import java.util.Vector;
import java.util.Random;

/**
 * Created by Clemencio Morales Lucas.
 */

public class ParticleSwarmOptimization implements ParticleSwarmOptimizationConstants {

    private Vector<Particle> swarm = new Vector<>();
    private double[] pBest = new double[SWARM_SIZE];
    private Vector<Location> pBestLocation = new Vector<>();
    private double gBest;
    private Location gBestLocation;
    private double[] fitnessValueList = new double[SWARM_SIZE];
    private Random random = new Random();

    public void execute(final boolean verboseMode) {
        int iteration = 0;
        double w, error = 9999;

        this.initializeSwarm();
        this.updateFitnessList();

        for(int i=0; i < SWARM_SIZE; i++) {
            pBest[i] = fitnessValueList[i];
            pBestLocation.add((Location) swarm.get(i).getLocation());
        }

        while(iteration < MAX_ITERATION && error > ERR_TOLERANCE) {
            updatePersonalBest();
            updateGlobalBest(iteration);

            w = W_UPPER_BOUND - (((double) iteration) / MAX_ITERATION) * (W_UPPER_BOUND - W_LOWER_BOUND);

            for(int i=0; i < SWARM_SIZE; i++) {
                double r1 = random.nextDouble();
                double r2 = random.nextDouble();
                Particle p = swarm.get(i);
                double[] newVelocity = updateVelocity(w, i, r1, r2, p);
                updateLocation(p, newVelocity);
            }

            //Observation: The function ought to be minimized, thus it means it's getting closer to 0
            error = Particle.evaluate(gBestLocation) - 0;

            if(verboseMode){
                printCurrentResults(iteration);
            }

            iteration++;
            updateFitnessList();
        }
        printFinalResults(iteration-1);
    }

    private double[] updateVelocity(final double w, final int i, final double r1, final double r2,
                                    final Particle currentParticle) {
        double[] newVelocity = new double[PROBLEM_DIMENSION];

        newVelocity[0] = (w * currentParticle.getVelocity().getValues()[0]) +
                (r1 * C1) * (pBestLocation.get(i).getValues()[0] - currentParticle.getLocation().getValues()[0]) +
                (r2 * C2) * (gBestLocation.getValues()[0] - currentParticle.getLocation().getValues()[0]);

        newVelocity[1] = (w * currentParticle.getVelocity().getValues()[1]) +
                (r1 * C1) * (pBestLocation.get(i).getValues()[1] - currentParticle.getLocation().getValues()[1]) +
                (r2 * C2) * (gBestLocation.getValues()[1] - currentParticle.getLocation().getValues()[1]);

        Velocity vel = new Velocity(newVelocity);
        currentParticle.setVelocity(vel);
        return newVelocity;
    }

    private void updateLocation(Particle particle, double[] newVelocity) {
        double[] newLocation = new double[PROBLEM_DIMENSION];

        newLocation[0] = particle.getLocation().getValues()[0] + newVelocity[0];
        newLocation[1] = particle.getLocation().getValues()[1] + newVelocity[1];

        final Location location = new Location(newLocation);
        particle.setLocation(location);
    }

    private void updateGlobalBest(final int iteration) {
        int bestParticleIndex = getMinimumPosition(fitnessValueList);
        if(iteration == 0 || fitnessValueList[bestParticleIndex] < gBest) {
            gBest = fitnessValueList[bestParticleIndex];
            gBestLocation = (Location) swarm.get(bestParticleIndex).getLocation();
        }
    }

    private void updatePersonalBest() {
        for(int i=0; i < SWARM_SIZE; i++) {
            if(fitnessValueList[i] < pBest[i]) {
                pBest[i] = fitnessValueList[i];
                pBestLocation.set(i, (Location) swarm.get(i).getLocation());
            }
        }
    }

    public void initializeSwarm() {
        Particle p;
        for(int i=0; i<SWARM_SIZE; i++) {
            p = new Particle();
            PhysicalMagnitude location = randomLocationInitialization();
            PhysicalMagnitude velocity = randomVelocityInitialization();

            p.setLocation(location);
            p.setVelocity(velocity);
            swarm.add(p);
        }
    }

    private PhysicalMagnitude randomVelocityInitialization() {
        double[] vel = new double[PROBLEM_DIMENSION];

        vel[0] = Particle.VELOCITY_LOW + random.nextDouble() * (Particle.VELOCITY_HIGH - Particle.VELOCITY_LOW);
        vel[1] = Particle.VELOCITY_LOW + random.nextDouble() * (Particle.VELOCITY_HIGH - Particle.VELOCITY_LOW);
        return new Velocity(vel);
    }

    private PhysicalMagnitude randomLocationInitialization() {
        double[] loc = new double[PROBLEM_DIMENSION];

        loc[0] = Particle.LOCATION_X_LOW + random.nextDouble() * (Particle.LOCATION_X_HIGH - Particle.LOCATION_X_LOW);
        loc[1] = Particle.LOCATION_Y_LOW + random.nextDouble() * (Particle.LOCATION_Y_HIGH - Particle.LOCATION_Y_LOW);
        return new Location(loc);
    }

    public void updateFitnessList() {
        for(int i=0; i < SWARM_SIZE; i++) {
            fitnessValueList[i] = swarm.get(i).getFitnessValue();
        }
    }

    private void printCurrentResults(final int iterationNumber) {
        System.out.println();
        System.out.println(ITERATION_NUMBER_MESSAGE + iterationNumber + COLON_SEPARATOR);
        System.out.println(BEST_X_VALUE_MESSAGE + gBestLocation.getValues()[0]);
        System.out.println(BEST_Y_VALUE_MESSAGE + gBestLocation.getValues()[1]);
        System.out.println(EVALUATION_VALUE_MESSAGE + Particle.evaluate(gBestLocation));
    }

    private void printFinalResults(final int iterationNumber){
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println(SOLUTION_FOUND_AT_ITERATION_MESSAGE + (iterationNumber) + VALUES_MESSAGE);
        System.out.println(BEST_X_MESSAGE + gBestLocation.getValues()[0]);
        System.out.println(BEST_Y_MESSAGE + gBestLocation.getValues()[1]);
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
