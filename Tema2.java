import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Tema2 {
    public static AtomicInteger InQueue = new AtomicInteger(0);
    public static File orderProut = new File("order_products_out.txt");
    public static File orderout = new File("orders_out.txt");
    public static void main(String[] args) throws IOException {

        // Folosesc BufferedWriter pentru a scrie in fisiere
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("orders_out.txt"));
            writer.write("");
            writer.flush();
            writer.close();
            BufferedWriter writer1 = Files.newBufferedWriter(Paths.get("order_products_out.txt"));
            writer1.write("");
            writer1.flush();
            writer1.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        // Verific dacă au fost furnizate toate argumentele
        if (args.length < 2) {
            System.out.println("Argumentele trebui sa fie: <Numele folder-ului> <Numarul de Thread-uri> ");
            System.exit(1);
        }
        String orders = args[0] + "/orders.txt";
        String orprod = args[0] + "/order_products.txt";
        Integer NUM_THREADS = Integer.parseInt(args[1]);

        // Creez cate un executor service pentru fiecare nivel
        ExecutorService tpe = Executors.newFixedThreadPool(NUM_THREADS);
        ExecutorService tpe2 = Executors.newFixedThreadPool(NUM_THREADS);

        // Folosesc Reader pentru a citi fisierul de comenzi
        BufferedReader reader = new BufferedReader(new FileReader(orders));

        for(int i = 0 ; i < NUM_THREADS; i++){
            InQueue.incrementAndGet();
            tpe.submit(new MyRunnable(tpe, tpe2, reader, orprod, NUM_THREADS));

        }
        // Astept pana cand termina toate thread-urile
        while (InQueue.get() > 0){

        }
        tpe.shutdown();
        tpe2.shutdown();
    }

}