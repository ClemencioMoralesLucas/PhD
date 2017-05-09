package phd.cml.parallel;
//TODO DELETE
import phd.cml.fireworks.Main;
import phd.cml.parallel.ClassicFWA;
import phd.cml.parallel.ImprovedFWA;

/**
 * Created by Clemencio Morales Lucas.
 */
public class ParallelMain {

    public static final String OUTPUT_SEPARATOR = "========================================================================";

    public static void main(String[] args) {
//        executeHardwareWarmup();

/*todo seguir: va teniendo buena pinta. Queda por hacer:
           1) Arreglar output, probablemente haya que poner sync en algun sitio
           2) Meter mejorilla
           3) Ejecutarlo cien mil veces y coger los resultados
           4) <opc> ver si BBOB
           5 Preparar artículo
        */
        ClassicFWA classicFWA = new ClassicFWA();
        ImprovedFWA improvedFWA = new ImprovedFWA();

//        try {
//            classicFWA.thread.join();
//            improvedFWA.thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("===================================================RESULTS==========================================================");
//        Ahmdal's Acceleration Formula = timeWithoutImprovement/timeWithImprovement;
        final double ahmdalAcceleration = ((double) ClassicFWA.TIME_WITHOUT_IMPROVEMENT/ ImprovedFWA.TIME_WITH_IMPROVEMENT)*100;
        System.out.println("Ahmdal's Law = Non-Improved time /  Improved Time = " + ClassicFWA.TIME_WITHOUT_IMPROVEMENT + "/" + ImprovedFWA.TIME_WITH_IMPROVEMENT + " = " + ahmdalAcceleration);
//
        printResultsDependingOnAhmdalsAcceleration(ahmdalAcceleration);
    }

    private static void executeHardwareWarmup() {
        for (int i = 0; i < 20; i++) {
            new Main().launch(Main.HostSystem.WINDOWS, false);
            new Main().launch(Main.HostSystem.WINDOWS, true);
        }
        System.out.println("----------------------------------------------------HARDWARE WARMUP PHASE ENDED---------------------------------------------------------------");
    }

    private static void printResultsDependingOnAhmdalsAcceleration(double ahmdalAcceleration) {
        ahmdalAcceleration -= 100;
        if(ahmdalAcceleration >= 1){
            System.out.println("The improved algorithm is a "+ahmdalAcceleration+ "% faster");
        } else if (ahmdalAcceleration < 1) {
            System.out.println("No improvement. The new algorithm is "+Math.abs(ahmdalAcceleration)+"% slower");
        }
    }
}

