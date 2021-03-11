package charlie.bs.section2;

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
 * Tests my 5 vs the dealer 2 which should be HIT.
 */
public class Test02_5_2 {
    @Test
    public void test() {
        Hand myHand = new Hand(new Hid(Seat.YOU));
        
        Card card1 = new Card(3,Card.Suit.CLUBS);
        Card card2 = new Card(2,Card.Suit.HEARTS);
        
        myHand.hit(card1);
        myHand.hit(card2);
        
        Card upCard = new Card(2,Card.Suit.SPADES);
        
        IAdvisor advisor = new Advisor();
        
        Play advice = advisor.advise(myHand,upCard);
        
        assertEquals(advice, Play.HIT);
    }
}
