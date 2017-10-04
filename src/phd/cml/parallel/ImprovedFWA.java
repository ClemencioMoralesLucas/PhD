package phd.cml.parallel;
//TODO DELETE
import phd.cml.fireworks.Main;

import java.io.FileNotFoundException;

/**
 * Created by Clemencio Morales Lucas.
 */
public class ImprovedFWA implements Runnable {

    public static final String OUTPUT_SEPARATOR = "=======================================";
    public Thread thread;
    public static long TIME_WITH_IMPROVEMENT;

    public ImprovedFWA() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        long initialTimeImp = System.nanoTime();


        //TODO Traer aquí la lógica EN VEZ DE ESTA LINEA
        try {
            new Main().launch(Main.HostSystem.UNIX, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        for (int i = 0; i < 10; i++) {
//            System.out.println("B-Improved");
//        }

        long finalTimeImp = System.nanoTime();
        ImprovedFWA.TIME_WITH_IMPROVEMENT = finalTimeImp - initialTimeImp;
//        System.out.println(OUTPUT_SEPARATOR);
//        System.out.println("Time elapsed for improved algorithm (ns): "+ ImprovedFWA.TIME_WITH_IMPROVEMENT);
    }
}
