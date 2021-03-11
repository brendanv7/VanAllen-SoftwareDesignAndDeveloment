package concurrency.actor.remote;

import com.googlecode.actorom.Actor;
import com.googlecode.actorom.annotation.OnMessage;
import com.googlecode.actorom.remote.ServerTopology;
import concurrency.actor.Ack;
import concurrency.actor.Good;
import java.util.Random;

/**
 * This class is the "scale-out" consumer actor.
 * @author Ron Coleman
 */
public class Consumer {
    public final static String HOST = "127.0.0.1";
    public final static int PORT = 8000;
    public final static String ID = "CONSUMER";
    protected final static long MEAN_DELAY = 1000;
    protected static ServerTopology serverTopology = new ServerTopology(HOST,PORT);
    protected Random ran = new Random();
    
    /**
     * Entry for the consumer.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        serverTopology.spawnActor(ID, new Consumer());  
    }
    
    /**
     * Processes a good.
     * @param good Good
     */
    @OnMessage(type = Good.class)
    public void onReceive(Good good) {
        try {
            long delay = Math.abs(ran.nextLong()) % (MEAN_DELAY*2);
            
            Thread.sleep(delay);   
            
            System.out.println("consumed "+good);
            
            // Send an ACK reply
            Actor actor = serverTopology.getActor(good.getSource());
            
            actor.send(new Ack(good.getId()));
            
        } catch (InterruptedException ex) {

        }
    }
}
