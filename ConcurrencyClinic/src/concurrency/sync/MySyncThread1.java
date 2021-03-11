package concurrency.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class demonstrates how to synchronize threads that
 * execute a critical region.
 * @author Ron Coleman
 */
public class MySyncThread1 implements Runnable {
    protected final static long MEAN_DELAY = 500;
    protected final static long POLL_DUTY_CYCLE_TIME = 1000;
    protected final List<Good> queue;
    
    public static void main(String[] args) {
        MySyncThread1 myThread = new MySyncThread1();
        
        new Thread(myThread).start();

        myThread.produce();
    }
    
    public MySyncThread1() {
        this.queue = new ArrayList<>( );
    }
    
    @Override
    public void run() {
        consume();
    }

    
    public void produce() {
        Random ran = new Random();
        for(int i=0; i < 10; i++) {
            long delay = Math.abs(ran.nextLong()) % (MEAN_DELAY * 2);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
            Good good = new Good();
            synchronized(queue) {
                queue.add(good);
            }
            System.out.println("produced "+good);
        }
    }
    
    public void consume() {
        while(true) {
            try {
                Thread.sleep(POLL_DUTY_CYCLE_TIME);
                
                synchronized(queue) {
                    if(queue.isEmpty())
                        continue;
                    
                    Good good = queue.remove(0);
                    
                    System.out.println("consumed "+good);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MySyncThread1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
}
