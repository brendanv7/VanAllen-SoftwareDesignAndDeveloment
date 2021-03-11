package concurrency.thread;

/**
 * This class demonstrates how to start and stop an anonymous Runnable
 * @author Ron Coleman
 */
public class MyAnonRunnable {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("in my runnable...");
            }
        }
        ).start();
       
        int hyperthreads = Runtime.getRuntime().availableProcessors();
        
        System.out.println("in main hyperthreads  = "+hyperthreads);        
    }    
}
