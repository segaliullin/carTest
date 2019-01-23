import java.io.BufferedWriter;
import java.io.IOException;

public class SynchronizedLineWriter {

    private static volatile int lineNumber = 0;
    private static final Object lock = new Object();

    public void writeLineAndIncrementLineNumber(BufferedWriter writer, LineNumberAndValuePair line){
        synchronized (lock) {
            if (line.getLineNumber() != lineNumber) {
                return;
            }

            try {
                writer.write(line.getValue());
                writer.newLine();
                lineNumber++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
