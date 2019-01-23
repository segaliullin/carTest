public class SynchronizedLengthCounter {

    private static int totalLength = 0;
    private static final Object lock = new Object();

    public void incrementTotal(int length){
        synchronized (lock) {
            totalLength += length;
        }
    }

    public int getTotalLength() {
        return totalLength;
    }
}
