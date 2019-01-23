import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class LineProcessorRunnable implements Runnable {

    private BlockingQueue<LineNumberAndValuePair> inputQueue;
    private Queue<LineNumberAndValuePair> outputQueue;
    private SynchronizedLengthCounter synchronizedLengthCounter;

    public LineProcessorRunnable(BlockingQueue<LineNumberAndValuePair> inputQueue,
                                 Queue<LineNumberAndValuePair> outputQueue,
                                 SynchronizedLengthCounter synchronizedLengthCounter) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.synchronizedLengthCounter = synchronizedLengthCounter;
    }

    @Override
    public void run() {
        System.out.println("LineProcessor " + Thread.currentThread().getId() + ": ready for duty...");
        while (true) { // dangerous
            LineNumberAndValuePair line = null;
            try {
                line = inputQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (line != null) {
                if (line.getLineNumber() == -1) {
                    outputQueue.add(line); // Passing thru killer object before shutdown
                    System.out.println("LineProcessor " + Thread.currentThread().getId() + ": killer object received, shutting down");
                    return;
                }

                String lineValue = line.getValue();
                int lineLength = lineValue.length();
                line.setValue(
                        new StringBuilder()
                                .append(lineValue)
                                .append(" - ")
                                .append(lineLength)
                                .toString()
                );

                synchronizedLengthCounter.incrementTotal(lineLength);
                outputQueue.add(line);
            }
        }
    }
}
