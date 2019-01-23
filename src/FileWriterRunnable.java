import java.io.BufferedWriter;
import java.util.Iterator;
import java.util.Queue;

public class FileWriterRunnable implements Runnable {

    private Queue<LineNumberAndValuePair> outputQueue;
    private SynchronizedLineWriter synchronizedLineWriter;
    private BufferedWriter bufferedWriter;

    private boolean shutdownOnEmptyFlag = false;

    public FileWriterRunnable(Queue outputQueue,
                              SynchronizedLineWriter synchronizedLineWriter,
                              BufferedWriter bufferedWriter) {
        this.outputQueue = outputQueue;
        this.synchronizedLineWriter = synchronizedLineWriter;
        this.bufferedWriter = bufferedWriter;
    }

    @Override
    public void run() {
        System.out.println("FileWriter " + Thread.currentThread().getId() + ": ready for duty...");
        while (true) {
            int requestedLineNumber = synchronizedLineWriter.getLineNumber();
            boolean hasData = false; // suppose there no data in queue
            Iterator<LineNumberAndValuePair> it = outputQueue.iterator();

            while (it.hasNext()) { // according to implementation of queue, we will iterate from oldest line to newest one
                LineNumberAndValuePair line = it.next();
                if (line.getLineNumber() == -1) {
                    if (!shutdownOnEmptyFlag) {
                        System.out.println("FileWriter " + Thread.currentThread().getId() + ": killer object received, shutting down on empty queue");
                    }
                    shutdownOnEmptyFlag = true;
                    continue;
                }

                hasData = true;
                if (line.getLineNumber() == requestedLineNumber) {
                    it.remove();
                    synchronizedLineWriter.writeLineAndIncrementLineNumber(bufferedWriter, line);
                    requestedLineNumber = synchronizedLineWriter.getLineNumber();
                }
            }

            if (shutdownOnEmptyFlag && !hasData) {
                System.out.println("FileWriter " + Thread.currentThread().getId() + ": shutting down");
                return;
            }
        }
    }
}
