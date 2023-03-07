import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MyRunnable2 implements Runnable {
    BufferedReader reader2;
    ExecutorService tpe, tpe2;
    String lineOrd;
    Semaphore semaphore;
    public MyRunnable2(ExecutorService tpe, ExecutorService tpe2, BufferedReader reader2, String lineOrd, Semaphore semaphore) {
        this.tpe = tpe;
        this.tpe2 = tpe2;
        this.reader2 = reader2;
        this.lineOrd = lineOrd;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        String line = null;
        try {
            line = reader2.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (line != null) {
            String[] parts = line.split(",");
            String[] partsOrd = lineOrd.split(",");
            // Primul element din parts este numele comenzii, al doilea este numele produsului
            String commandName = parts[0];
            String orderName = partsOrd[0];

            // Daca numele comenzii curente corespunde cu numele comenzii cautate
            if (commandName.equals(orderName)) {
                // incrementez la fiecare produs gasit
                semaphore.release();

                // Se scrie in fisierul de iesire faptul ca produsul curent a fost trimis
                String shipped = line + ",shipped\n";
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(Tema2.orderProut, true));
                    writer.append(shipped);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Citesc urmatoarea linie
            try {
                line = reader2.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
