import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MyRunnable implements Runnable {
    BufferedReader reader;
    String orprod;
    ExecutorService tpe, tpe2;
    Integer NUM_THREADS;

    public MyRunnable(ExecutorService tpe, ExecutorService tpe2, BufferedReader reader, String orprod, Integer NUM_THREADS) {
        this.tpe = tpe;
        this.tpe2 = tpe2;
        this.reader = reader;
        this.orprod = orprod;
        this.NUM_THREADS = NUM_THREADS;
    }

    @Override
    public void run() {
        String line = new String();

        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] parts = line.split(",");
            // Primul element din parts este numele comenzii, al doilea este numărul de produse
            Semaphore semaphore = new Semaphore(-Integer.parseInt(parts[1]) + 1);

            if (Integer.parseInt(parts[1]) != 0) {
                // Creez un reader pentru fiecare comanda
                BufferedReader reader2 = null;
                try {
                    reader2 = new BufferedReader(new FileReader(orprod));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // Trimit task-uri noi pentru linia curentă către ExecutorService
                for (int i = 0; i < NUM_THREADS; i++)
                    tpe2.submit(new MyRunnable2(tpe, tpe2, reader2, line, semaphore));

                // Astept pana cand se citesc toate produsele
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Scriu in fisier faptul ca s-a trimis comanda
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(Tema2.orderout, true));
                    writer.append(parts[0] + "," + parts[1] + ",shipped\n");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                semaphore.release();
            }
        }
            // Fiecare thread termina de citit
            Tema2.InQueue.decrementAndGet();
        }
    }


