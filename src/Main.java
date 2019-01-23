import java.io.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        File file = new File(args[0]);

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("Specified input file not found. Exiting.");
        }

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(args[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bufferedReader == null || bufferedWriter == null) {
            return;
        }

        BlockingQueue inputQueue = new ArrayBlockingQueue(10); // default initial capacity
        ConcurrentLinkedQueue<LineNumberAndValuePair> outputQueue = new ConcurrentLinkedQueue<>();
        int availableCores = Runtime.getRuntime().availableProcessors(); // logical, not physical

        // First pool of threads: File readers
        SynchronizedLineReader synchronizedLineReader = new SynchronizedLineReader();
        ExecutorService fileReadExecutors = Executors.newFixedThreadPool(availableCores);
        for (int i = 0; i < availableCores; i++) {
            fileReadExecutors.submit(new FileReaderRunnable(bufferedReader, inputQueue, synchronizedLineReader));
        }

        // Second pool of threads: Line processors
        SynchronizedLengthCounter synchronizedLengthCounter = new SynchronizedLengthCounter();
        ExecutorService lineProcessorExecutors = Executors.newFixedThreadPool(availableCores);
        for (int i = 0; i < availableCores; i++) {
            lineProcessorExecutors.submit(new LineProcessorRunnable(inputQueue, outputQueue, synchronizedLengthCounter));
        }

        // Third pool of threads: File writers
        SynchronizedLineWriter synchronizedLineWriter = new SynchronizedLineWriter();
        ExecutorService fileWritersExecutors = Executors.newFixedThreadPool(availableCores);
        for (int i = 0; i < availableCores; i++) {
            fileWritersExecutors.submit(new FileWriterRunnable(outputQueue, synchronizedLineWriter, bufferedWriter));
        }

        // shutting down on all tasks (threads) completed
        fileReadExecutors.shutdown();
        lineProcessorExecutors.shutdown();
        fileWritersExecutors.shutdown();

        while (true) {
            try {
                Thread.sleep(1000);
                if (fileReadExecutors.isTerminated() && lineProcessorExecutors.isTerminated() && fileWritersExecutors.isTerminated()) {
                    bufferedReader.close();

                    bufferedWriter.write(String.valueOf(synchronizedLengthCounter.getTotalLength()));

                    bufferedWriter.flush();
                    bufferedWriter.close();

                    return; // end of program
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

    }
}
