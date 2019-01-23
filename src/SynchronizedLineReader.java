import java.io.BufferedReader;
import java.io.IOException;

public class SynchronizedLineReader {

    private static int lineNumber = 0;
    private static final Object lock = new Object();

    public LineNumberAndValuePair readLineAndIncrementLineNumber(BufferedReader reader){
        synchronized (lock) { // not best solution actually, because BufferedReader have almost same logic inside
            try {
                return new LineNumberAndValuePair(lineNumber++, reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
