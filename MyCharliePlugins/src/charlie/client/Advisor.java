package charlie.client;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.IAdvisor;
import charlie.util.Play;

/**
 * Implements the IAdvisor interface of Charlie.
 * Advisor gives advice to the player based on their hand
 * and the dealer's upCard. This Advisor uses the BasicStrategy
 * class to give advice.
 * 
 * @author Brendan Van Allen
 */
public class Advisor implements IAdvisor {

    @Override
    /**
     * Implements the advise method of the IAdvisor interface.
     * Uses the BasicStrategy class to determine what PLAY
     * to advise.
     */
    public Play advise(Hand myHand, Card upCard) {
        try{
            myHand.getCard(0).value();
            myHand.getCard(1).value();
            upCard.value();
        }
        catch(NullPointerException e) {
            System.out.println("Error: myHand, a card in myHand, or upCard was null");
        }
        catch(IndexOutOfBoundsException e){ 
            System.out.println("Error: There was less than 3 cards in myHand.");
        }
        return BasicStrategy.getPlay(myHand,upCard);
    }
}
