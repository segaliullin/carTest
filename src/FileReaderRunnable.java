import java.io.BufferedReader;
import java.util.concurrent.BlockingQueue;

public class FileReaderRunnable implements Runnable {

    private BufferedReader reader;
    private BlockingQueue<LineNumberAndValuePair> queue;
    private SynchronizedLineReader synchronizedLineReader;

    public FileReaderRunnable(BufferedReader reader,
                              BlockingQueue<LineNumberAndValuePair> queue,
                              SynchronizedLineReader synchronizedLineReader) {
        this.reader = reader;
        this.queue = queue;
        this.synchronizedLineReader = synchronizedLineReader;
    }

    @Override
    public void run() {
        System.out.println("FileReader " + Thread.currentThread().getId() + ": ready for duty...");
        try {
            LineNumberAndValuePair line = synchronizedLineReader.readLineAndIncrementLineNumber(reader);
            while (line.getValue() != null) {
                queue.put(line);
                line = synchronizedLineReader.readLineAndIncrementLineNumber(reader);
            }
            queue.put(LineNumberAndValuePair.createKillerObject());
            System.out.println("FileReader " + Thread.currentThread().getId() + ": killer object sent, shutting down");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
