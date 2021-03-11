package concurrency.actor.remote;

import com.googlecode.actorom.Actor;
import com.googlecode.actorom.Address;
import com.googlecode.actorom.annotation.OnMessage;
import com.googlecode.actorom.remote.ServerTopology;
import concurrency.actor.Ack;
import concurrency.actor.Good;
import java.util.Random;

/**
 * This class is the "scale-out" producer actor.
 * @author Ron Coleman
 */
public class Producer {
    public final static String HOST = "127.0.0.1";
    public final static int PORT = 9000;
    public final static String ID = "PRODUCER";
    protected final static long MEAN_DELAY = 500;
    protected Actor consumer;
    protected Address myAddress;

    /**
     * Entry point for the producer.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        ServerTopology serverTopology = new ServerTopology(Producer.HOST,Producer.PORT);
        
        Address consumerAddr =
                Address.newRemoteAddress(Consumer.HOST, Consumer.PORT, Consumer.ID);
        
        Actor consumerActor = serverTopology.getActor(consumerAddr);
        
        Producer producer = new Producer(consumerActor);
        
        Address producerAddr =
                serverTopology.spawnActor(Producer.ID, producer);
        
        producer.setMyAddress(producerAddr);
    }

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
                
                consumer.send(new Good(myAddress));
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
