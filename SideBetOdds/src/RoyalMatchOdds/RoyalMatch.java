package RoyalMatchOdds;

import charlie.card.Card;
import charlie.card.Shoe;

/**
 * This program estimates the odds of Royal Match using Monte Carlo simulation.
 * @author Brendan Van Allen
 */
public class RoyalMatch {
    public final static int NUM_SAMPLES = 1000000;

    public static void main(String[] args) { 
        
        // Only need a one deck, standard shoe
        Shoe shoe = new Shoe(1);
               
        // Frequency we draw suited King and Queen as our first 2 cards
        int count = 0;
        
        // Main loop of the simulation
        for(int sim=0; sim < NUM_SAMPLES; sim++) {
            
            // Initialize the shoe--reloads the shoe and shuffles
            shoe.init();
        
            // Get our first card
            Card card1 = shoe.next();
            
            // Dealer's hole card
            shoe.next();
            
            // Get our 2nd card
            Card card2 = shoe.next();
            
            // Dealer's up-card
            shoe.next();
            
            // If our cards are King and Queen of same suit, count it
            if(card1.getRank() == Card.KING) {
                if(card2.getRank() == Card.QUEEN && card1.getSuit().equals(card2.getSuit())) {
                    count++;
                }
            } else if(card1.getRank() == Card.QUEEN) {
                if(card2.getRank() == Card.KING && card1.getSuit().equals(card2.getSuit())) {
                    count++;
                }
            }
                
        }
        
        // Compute the estimate probablity and odds
        double p = (count / (double)NUM_SAMPLES);
        
        int odds = (int) (((1 - p) / p) + 0.5);
        
        System.out.printf("Royal Match prob = %8.6f odds = %d:1\n", p, odds);
    }
    
        
}