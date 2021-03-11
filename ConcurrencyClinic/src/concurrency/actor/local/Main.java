package concurrency.actor.local;

import com.googlecode.actorom.Actor;
import com.googlecode.actorom.Address;
import com.googlecode.actorom.Topology;
import com.googlecode.actorom.local.LocalTopology;

/**
 * This class is launches producer and consumer actors in the same JVM to
 * scale-up producing.
 * @author Ron Coleman
 */
public class Main {
    public static void main(String[] args) {
        // Create a topology to scale up the actors
        Topology topology = new LocalTopology("local");
        
        // Spawn the consumer actor
        Address consumerAddr = topology.spawnActor(Consumer.ID,new Consumer());
        Actor consumerActor = topology.getActor(consumerAddr);
        
        // Spawn the producer actor
        Producer producer = new Producer(consumerActor);
        Address producerAddr = topology.spawnActor(Producer.ID, producer);
        
        producer.setMyAddress(producerAddr);
        
        // Start producing
        producer.produce();
    }
}
