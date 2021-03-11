package charlie.client;

import charlie.message.Message;
import charlie.message.view.to.Deal;
import charlie.message.view.to.GameStart;
import charlie.message.view.to.Shuffle;
import charlie.plugin.ITrap;
import static java.lang.Math.round;
import org.apache.log4j.Logger;

/**
 * Counts card using the Hi-Lo (1963) system and calculates
 * bets using Kelly's Criterion.
 * @author Brendan Van Allen
 */
public class CountingTrap implements ITrap{
    protected int decksInShoe;
    protected int cardsInShoe;
    protected int runningCount = 0;
    protected double trueCount;
    protected double bet;
    protected boolean shufflePending = false;
    private final Logger LOG = Logger.getLogger(CountingTrap.class);
    
    @Override
    /**
     * Not implemented
     */
    public void onSend(Message msg) {
        
    }

    @Override
    /**
     * Traps incoming messages.
     * 
     * When message is GameStart, we update the shoe size and check
     * if we need to reset counts due to shuffle.
     * 
     * When message is Deal, we check the card that was dealt and
     * update our counts.
     * 
     * When message is Shuffle, we set a flag so we know our counts
     * will need to be reset at the start of the next game.
     * 
     * @param msg The inbound Message
     */
    public void onReceive(Message msg) {
        
        if(msg instanceof GameStart) {       
            // Update the shoe size
            cardsInShoe = ((GameStart) msg).shoeSize();
            updateShoeSize();
            
            // Check if there was a shuffle
            if(shufflePending){
                shufflePending = false;
                runningCount = 0;
            }
        }
        
        else if (msg instanceof Deal) {
            
            // Ignore deal message if the card is null
            if(((Deal) msg).getCard() != null) {
                
                // Update the shoe size
                cardsInShoe--;
                updateShoeSize();
                
                // Check what card was dealt and update the runningCount
                int cardRank = ((Deal) msg).getCard().getRank();
                if(cardRank > 9 || cardRank == 1) 
                    runningCount--;
                else if (cardRank > 1 && cardRank < 7)
                    runningCount++;
                
                // Update trueCount and bet with new runningCount
                trueCount = (double) runningCount / (double) decksInShoe;
                bet = round(Math.max(1,trueCount+1));
                
                LOG.info("shoe size: "+cardsInShoe+" running count: "+runningCount
                         +" true count: "+trueCount+" bet: "+bet+" (chips)");
            }
        }
        
        else if (msg instanceof Shuffle) {          
            // Set flag so we know to reset counts at next GameStart
            shufflePending = true;
        }
    }
    
    /**
     * Helper method to recalculate the number of decks remaining
     * in the shoe.
     */
    private void updateShoeSize(){
        // shoeSize() gives the number of cards in the shoe, so must convert to number of decks
        decksInShoe = round((cardsInShoe / 52));
            
        // Since we can't have 0 decks in the shoe, we must correct
        if(decksInShoe == 0)
            decksInShoe = 1;
    }
}
