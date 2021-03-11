package concurrency.thread;

/**
 * This class demonstrates how to start a thread with a Runnable.
 * @author roncoleman125
 */
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("in my runnable...");
    }
    
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        new Thread(myRunnable).start();
       
        int hyperthreads = Runtime.getRuntime().availableProcessors();
        
        System.out.println("in main hyperthreads  = "+hyperthreads);        
    }
}
