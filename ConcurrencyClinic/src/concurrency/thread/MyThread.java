package concurrency.thread;

/**
 * This class demonstrates how to start and stop a thread.
 * @author Ron Coleman
 */
public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("in my thread...");
    }
    
    public static void main(String[] args) {       
        MyThread myThread = new MyThread();
        myThread.start();
        
        int hyperthreads = Runtime.getRuntime().availableProcessors();
        
        System.out.println("in main hyperthreads  = "+hyperthreads);        
    }
}
