package phd.cml.fireworks.algorithm.utils;

import java.util.Random;

/**
 * Created by Clemencio Morales Lucas
 */
public class FireworksAlgorithmUtils {

    public static int generateRandomValue(final int maxBound){
        return new Random().nextInt(maxBound);
    }
}
