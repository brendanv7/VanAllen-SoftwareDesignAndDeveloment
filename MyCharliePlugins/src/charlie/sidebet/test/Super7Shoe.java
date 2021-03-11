package charlie.sidebet.test;

import charlie.card.Card;
import charlie.card.Shoe;

/**
 * This shoe was created to test the Super 7s side bet.
 * @author Brendan Van Allen
 */
public class Super7Shoe extends Shoe {
    
    
    @Override
    /**
     * Overrides Shoe's init method to create a Shoe that
     * will test all 4 scenarios for the Super 7s side bet.
     */
    public void init() {
        cards.clear();
        
        // Scenario 1 from table
        cards.add(new Card(7,Card.Suit.CLUBS));             // YOU: 7
        cards.add(new Card(10,Card.Suit.DIAMONDS));         // Dealer: 10
        cards.add(new Card(Card.ACE,Card.Suit.HEARTS));     // YOU: 18
        cards.add(new Card(7,Card.Suit.SPADES));            // Dealer: 17
        
        // Scenario 2 from table
        cards.add(new Card(10,Card.Suit.CLUBS));            // YOU: 10
        cards.add(new Card(10,Card.Suit.DIAMONDS));         // Dealer: 10
        cards.add(new Card(Card.KING,Card.Suit.HEARTS));    // YOU: 20
        cards.add(new Card(3,Card.Suit.SPADES));            // Dealer: 13
        cards.add(new Card(4,Card.Suit.CLUBS));             // Hit, Dealer: 17
        
        // Scenario 3 from table
        cards.add(new Card(7,Card.Suit.CLUBS));             // YOU: 7
        cards.add(new Card(10,Card.Suit.DIAMONDS));         // Dealer: 10
        cards.add(new Card(6,Card.Suit.HEARTS));            // YOU: 13
        cards.add(new Card(Card.QUEEN,Card.Suit.SPADES));   // Dealer: 20
        cards.add(new Card(10,Card.Suit.DIAMONDS));         // Hit, YOU: 23
        
        // Scenario 4 from table
        cards.add(new Card(3,Card.Suit.CLUBS));             // YOU: 3
        cards.add(new Card(8,Card.Suit.DIAMONDS));          // Dealer: 8
        cards.add(new Card(9,Card.Suit.HEARTS));            // YOU: 12
        cards.add(new Card(10,Card.Suit.SPADES));           // Dealer: 18
        cards.add(new Card(2,Card.Suit.SPADES));            // Hit, YOU: 14
        cards.add(new Card(Card.QUEEN,Card.Suit.CLUBS));    // Hit, YOU: 24
    }
    
    @Override
    /**
     * Overrides Shoe's shuffleNeeded method to always
     * return false
     * 
     * @return false always
     */
    public boolean shuffleNeeded() {
        return false;
    }
}
