package mycharlie.plugin;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.IAdvisor;
import charlie.util.Play;

/**
 *
 * @author yanks
 */
public class SimpleAdvisor implements IAdvisor{

    @Override
    public Play advise(Hand myHand, Card upCard) {
        try{
            // Validating that Hand class and its methods are accessible, myHand is not null and has at least 2 cards
            myHand.getCard(1);
            myHand.size();
            myHand.getValue();
            
            // Validating that Card class and its value method is accessible, and that upCard is not null.
            upCard.value();
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("There's less than 2 cards in the hand.");
        }
        catch (NullPointerException e) {
            System.out.println("Either myHand or upCard was null.");
        }
        //catch (ClassNotFoundException { } 
        //catch (NoSuchMethodException e) { }
        Play play;
        if(myHand.size() == 2 && 
                ((myHand.getCard(0).value() == 1 && myHand.getCard(1).value() == 1) || 
                (myHand.getCard(0).value() == 8 && myHand.getCard(1).value()==8))) 
            play = Play.SPLIT;

         else if(myHand.getValue() >= 17) 
            play = Play.STAY;

         else if(myHand.getValue() <= 10) 
            play = Play.HIT;

         else if(myHand.getValue() == 11) {
            if(myHand.size() == 2) 
                play = Play.DOUBLE_DOWN;
            else
                play = Play.HIT;
         }

         else {
            if(upCard.value()+10 > 16)
                play = Play.HIT;
            else
                play = Play.STAY;
        }
        System.out.print("Advice: "+play);
        return play;
    }
}
