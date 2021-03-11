package charlie.bs.section1;

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
 * Tests my 20 vs the dealer 6 which should be STAY.
 */
public class Test00_20_6 {
    @Test
    public void test() {
        Hand myHand = new Hand(new Hid(Seat.YOU));
        
        Card card1 = new Card(10,Card.Suit.CLUBS);
        Card card2 = new Card(Card.JACK,Card.Suit.CLUBS);
        
        myHand.hit(card1);
        myHand.hit(card2);
        
        Card upCard = new Card(6,Card.Suit.SPADES);
        
        IAdvisor advisor = new Advisor();
        
        Play advice = advisor.advise(myHand,upCard);
        
        assertEquals(advice, Play.STAY);
    }
}
