package phd.cml.fireworks.algorithm;

import phd.cml.fireworks.algorithm.domain.Field;
import phd.cml.fireworks.algorithm.domain.Location;
import phd.cml.fireworks.algorithm.domain.Scenario;

/**
 * Created by Clemencio Morales Lucas on 07/07/2016.
 */
public class FWA {

    public static final String EMPTY_MATRIX = "The given matrix is empty";

    public static void main(String [] args){
        //TODO Algorithm implementation in http://www.cil.pku.edu.cn/research/fa/ Continue implementing it

        int height = 5, width = 5;
        Location [][] locations = new Location[height][width];
        Field field = new Field(locations);
        Scenario scenario = new Scenario(field);
        scenario.createScenario(height, width);

        printMatrix(scenario.getField().getLocations());
    }

    public static void printMatrix(Location[][] m){
        try{
            int rows = m.length;
            int columns = m[0].length;
            String str = "|\t";

            for(int i=0;i<rows;i++){
                for(int j=0;j<columns;j++){
                    str += m[i][j].isOptimal() + "\t";
                }

                System.out.println(str + "|");
                str = "|\t";
            }

        }catch(Exception e){System.out.println(EMPTY_MATRIX);}
    }
}
