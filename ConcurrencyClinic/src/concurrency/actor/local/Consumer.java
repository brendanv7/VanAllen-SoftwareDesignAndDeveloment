/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package concurrency.actor.local;

import com.googlecode.actorom.Topology;
import com.googlecode.actorom.annotation.OnMessage;
import com.googlecode.actorom.annotation.TopologyInstance;
import concurrency.actor.Good;
import java.util.Random;

/**
 * This class is the consumer actor.
 * @author Ron Coleman
 */
public class Consumer {
    public final static String ID = "CONSUMER";
    protected final static long MEAN_DELAY = 2000;
    protected Random ran = new Random();
    @TopologyInstance protected Topology topology;
    
    /**
     * Receives a good and processes it.
     * @param good Good
     */
    @OnMessage(type = Good.class)
    public void onReceive(Good good) {
        try {
            long delay = Math.abs(ran.nextLong()) % (MEAN_DELAY*2);
            
            Thread.sleep(delay);
            
            System.out.println("consumed "+good);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }    
}
