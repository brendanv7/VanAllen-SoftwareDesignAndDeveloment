package concurrency.sync;

/**
 * This class is a "good" sent from producer to consumer.
 * @author Ron Coleman
 */
public class Good {
    private static int serialno = 0;
    
    private final int id;
    
    public Good() {
        this.id = serialno++;
    }

    public int getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return "good id = "+getId();
    }
}
