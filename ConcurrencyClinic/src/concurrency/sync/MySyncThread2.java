package concurrency.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class demonstrates how to synchronize threads
 * using synchronized methods.
 * @author Ron Coleman
 */
public class MySyncThread2 implements Runnable {
    protected final static long MEAN_DELAY = 500;
    protected final static long POLL_DUTY_CYCLE = 1000;
    protected final List<Good> queue;
    
    public static void main(String[] args) {
        MySyncThread2 myThread = new MySyncThread2();
        
        new Thread(myThread).start();

        myThread.produce();
    }
    
    public MySyncThread2() {
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
            
            add(good);
            
            System.out.println("produced "+good);
        }
    }
    
    public void consume() {
        while(true) {
            try {
                Thread.sleep(POLL_DUTY_CYCLE);
                Good good = remove();
                
                if(good == null)
                    continue;
                
                System.out.println("consumed "+good);
            } catch (InterruptedException ex) {
                Logger.getLogger(MySyncThread1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    public synchronized void add(Good good) {
        queue.add(good);
    }
    
    public synchronized Good remove() {
        if(queue.isEmpty())
            return null;
        
        return queue.remove(0);
    }
}
