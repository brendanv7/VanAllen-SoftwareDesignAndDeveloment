package charlie.bs.section4;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.client.Advisor;
import charlie.dealer.Seat;
import charlie.plugin.IAdvisor;
import charlie.util.Play;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests my pair of 5's vs the dealer 2 which should be DOUBLE_DOWN.
 */
public class Test01_55_2 {
    @Test
    public void test() {
        Hand myHand = new Hand(new Hid(Seat.YOU));
        
        Card card1 = new Card(5,Card.Suit.CLUBS);
        Card card2 = new Card(5,Card.Suit.DIAMONDS);
        
        myHand.hit(card1);
        myHand.hit(card2);
        
        Card upCard = new Card(2,Card.Suit.SPADES);
        
        IAdvisor advisor = new Advisor();
        
        Play advice = advisor.advise(myHand,upCard);
        
        assertEquals(advice, Play.DOUBLE_DOWN);
    }
}
