package phd.cml.parallel;
//TODO DELETE
import phd.cml.fireworks.Main;

import java.io.FileNotFoundException;

import static phd.cml.fireworks.Main.INFORMATION_FILE_PATH_UNIX;
import static phd.cml.fireworks.Main.INFORMATION_FILE_PATH_WINDOWS;

/**
 * Created by Clemencio Morales Lucas.
 */
public class ClassicFWA implements Runnable {

    public static final String OUTPUT_SEPARATOR = "======================================";
    public Thread thread;
    public static long TIME_WITHOUT_IMPROVEMENT;

    public ClassicFWA() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        //TODO Find a way of reformatting the table
        long initialTime = System.nanoTime();

        //TODO Traer aquí la lógica EN VEZ DE ESTA LINEA
        try {
            new Main().launch(Main.HostSystem.UNIX, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//for (int i = 0; i < 10; i++) {
//    System.out.println("A");
//}

        long finalTime = System.nanoTime();
        TIME_WITHOUT_IMPROVEMENT = finalTime - initialTime;
//        System.out.println(OUTPUT_SEPARATOR);
//        System.out.println("Time elapsed for current algorithm (ns): "+ TIME_WITHOUT_IMPROVEMENT);
//        System.out.println(OUTPUT_SEPARATOR);

    }

}
