package Super7Odds;

import charlie.card.Card;
import charlie.card.Shoe;

/**
 * This program estimates the odds of Super 7 using Monte Carlo simulation.
 * @author Ron.Coleman
 */
public class Super7 {
    public final static int NUM_SAMPLES = 100000;

    public static void main(String[] args) { 
        
        // Only need a one deck, standard shoe
        Shoe shoe = new Shoe(1);
               
        // Frequency we draw a 7 as our first card
        int count = 0;
        
        // Main loop of the simulation
        for(int sim=0; sim < NUM_SAMPLES; sim++) {
            
            // Initialize the shoe--reloads the shoe and shuffles
            shoe.init();
        
            // Get our card
            Card card = shoe.next();
            
            // This point forward starts Blackjack but it does not affect Super 7
            
            // Dealer's hole card
            shoe.next();
            
            // My next card
            shoe.next();
            
            // Dealer's up-card
            shoe.next();
            
            // If our first card is a 7, count it.
            if(card.getRank() == 7)
                count++;
        }
        
        // Compute the estimate probablity and odds
        double p = (count / (double)NUM_SAMPLES);
        
        int odds = (int) (((1 - p) / p) + 0.5);
        
        System.out.printf("Super 7 prob = %8.6f odds = %d:1\n", p, odds);
    }
    
        
}