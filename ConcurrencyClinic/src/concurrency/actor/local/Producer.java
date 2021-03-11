package concurrency.actor.local;

import com.googlecode.actorom.Actor;
import com.googlecode.actorom.Address;
import com.googlecode.actorom.annotation.OnMessage;
import concurrency.actor.Ack;
import concurrency.actor.Good;
import java.util.Random;

/**
 * This class is the producer actor.
 * @author Ron Coleman
 */
public class Producer {
    public final static String ID = "PRODUCER";
    protected final static long MEAN_DELAY = 500;
    protected Actor consumer;
    protected Address myAddress;

    /**
     * Constructor
     * @param consumer Actor
     */
    public Producer(Actor consumer) {
        this.consumer = consumer;
    }
    
    /**
     * Produce goods
     */
    public void produce() {
        Random ran = new Random();
        for(int i=0; i < 10; i++) {
            try {
                long delay = Math.abs(ran.nextLong()) % (MEAN_DELAY * 2);
                
                Thread.sleep(delay);
                
                Good good = new Good(myAddress);
                
                consumer.send(good);
                
                System.out.println("produced "+good);
            }
            catch (InterruptedException ex) {
            }
        }
    }
    
    /**
     * Sets my actor address.
     * @param address Address
     */
    public void setMyAddress(Address address) {
        this.myAddress = address;
    }
    
    /**
     * Processes an acknowledgment.
     * @param ack Ack message
     */
    @OnMessage(type = Ack.class)
    public void onReceive(Ack ack) {
        System.out.println("received "+ack);
    }    
}
