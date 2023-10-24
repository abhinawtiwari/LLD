public class ProducerConsumer {
    static Object key = new Object();
    private static boolean[] buffer;
    private static int currentSize;

    public static void main(String[] args) throws Exception {
        buffer = new boolean[10];
        currentSize = 0;

        Runnable prodInst = new Runnable() {
            @Override
            public void run() {
                try {
                    producer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable consInst = new Runnable() {
            @Override
            public void run() {
                try {
                    consumer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread prodThread = new Thread(prodInst);
        Thread consThread = new Thread(consInst);

        prodThread.start();
        consThread.start();

        prodThread.join();
        consThread.join();
    }
    public static void producer() throws InterruptedException {
        while (true) {
            synchronized (key) {
                if (isFull(buffer)) {
                    System.out.print("waiting - buffer full");
                    key.wait();
                } else {
                    buffer[currentSize++] = true;
                    key.notify();
                }
            }
        }
    }
    private static boolean isFull(boolean[] buffer2) {
        if(currentSize == buffer2.length) return true;
        return false;
    }
    public static void consumer() throws InterruptedException {
        while (true) {
            synchronized (key) {
                if (isEmpty(buffer)) {
                    System.out.print("waiting - buffer empty\n");
                    key.wait();
                } else {
                    buffer[--currentSize] = false;
                    key.notify();
                }
            }
        }
    }
    private static boolean isEmpty(boolean[] buffer2) {
        if(currentSize == 0) return true;
        return false;
    }
}
