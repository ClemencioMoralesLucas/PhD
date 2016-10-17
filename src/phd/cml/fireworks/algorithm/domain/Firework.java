package phd.cml.fireworks.algorithm.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clemencio Morales Lucas
 */
public class Firework {

    private List<Spark> sparkList;

    public Firework(final List<Spark> sparkList){
        //determineNumberOfSparks();//TODO Think were can we place this
        //calculateAmplitudeForEachExplosion();
        this.setSparkList(sparkList);
    }

    public List<Spark> getSparkList() {
        return sparkList;
    }

    public void setSparkList(List<Spark> sparkList) {
        this.sparkList = sparkList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Firework firework = (Firework) o;

        return sparkList.equals(firework.sparkList);

    }

    @Override
    public int hashCode() {
        return sparkList.hashCode();
    }

    @Override
    public String toString() {
        return "Firework{" +
                "sparkList=" + sparkList +
                '}';
    }

    public void fire(){
        boolean end = false;
        do{
            setOffInitialLocations(null);//TODO This has to be the number of locations
            setOffFireworksInSelectedLocations();
            obtainLocationOfSparks();
            evaluateLocationsQuality();
            if(optimalLocationFound()){
                end = true;
            }
        } while (!end);
    }

    public void setOffInitialLocations(final Integer numberOfLocations){

    }

    private void setOffFireworksInSelectedLocations() {
    }

    private void obtainLocationOfSparks() {
        /*TODO Remember: The sparks have to be spawned following a Gaussian mutation:
            -Choose firework from population
            -Apply Gaussian mutation to firework in randomly selected locations
            -GM: Generate sparks between best firework and selected firework

            To further improve the diversity of a population, the Gaussian mutation is introduced to FWA. The way of
            producing sparks by Gaussian mutation is as follows: choose a firework from the current population, then
            apply Gaussian mutation to the firework in randomly selected dimensions.
            For Gaussian mutation, the new sparks are generated between the best firework and the selected fireworks.
            Still, Gaussian mutation may produce sparks exceed the feasible space. When a spark lies beyond the upper
            or lower boundary, the mapping rule will be carried out to map the spark to a new location within the feasible
            space.
        */

    }

    private void evaluateLocationsQuality() {

    }

    private boolean optimalLocationFound() {
        return false;
    }
}
