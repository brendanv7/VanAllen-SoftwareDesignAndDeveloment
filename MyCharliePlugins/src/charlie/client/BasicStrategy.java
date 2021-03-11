package charlie.client;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.util.Play;

/**
 * A reusable class that recommends a PLAY based on the player's
 * hand and the dealer's upCard according to the Basic Strategy
 * of Blackjack.
 * 
 * @author Brendan Van Allen
 */
public class BasicStrategy {
    private static final Play H = Play.HIT;
    private static final Play S = Play.STAY;
    private static final Play D = Play.DOUBLE_DOWN;
    private static final Play P = Play.SPLIT;
    
    /**
     * Determines which section of the Basic Strategy table to use
     * and calls the corresponding method.
     * @param myHand The Player's hand
     * @param upCard The Dealer's upCard
     * @return A value of the enumerated type Play
     */
    public static Play getPlay(Hand myHand, Card upCard) {
        
        // Sections 3 and 4 can only be used if there are exactly 2 cards in myHand
        if(myHand.size() == 2){
            
            // myHand must be checked for a pair before anything else
            if(myHand.getCard(0).getRank() == myHand.getCard(1).getRank())
                return section4(myHand,upCard);
            
            // If not a pair, then we must see if there is an Ace in myHand
            else if(myHand.getCard(0).isAce() || myHand.getCard(1).isAce())
                return section3(myHand,upCard);
        } 
        
        // If there are more than 2 cards in myHand, or there was no pair or Ace, we used section 1 or 2 based on the value of myHand
        if(myHand.getValue() < 12)
            return section2(myHand,upCard);
        else
            return section1(myHand,upCard);
        
    }
    
    /**
     * Recommends a Play from the 4th section of the basic strategy table.
     * @param myHand The Player's hand
     * @param upCard The Dealer's upCard
     * @return A value of Play from the 4th section.
     */
    public static Play section4(Hand myHand, Card upCard) {       
        Play[][] section4Table = {
                   // upCard = A  2  3  4  5  6  7  8  9 10
           /* myHand =  A,A*/ {P, P, P, P, P, P, P, P, P, P},
           /*           2,2*/ {H, P, P, P, P, P, P, H, H, H},
           /*           3,3*/ {H, P, P, P, P, P, P, H, H, H},
           /*           4,4*/ {H, H, H, H, P, P, H, H, H, H},
           /*           5,5*/ {H, D, D, D, D, D, D, D, D, H},
           /*           6,6*/ {H, P, P, P, P, P, H, H, H, H},
           /*           7,7*/ {H, P, P, P, P, P, P, H, H, H},
           /*           8,8*/ {P, P, P, P, P, P, P, P, P, P},
           /*           9,9*/ {S, P, P, P, P, P, S, P, P, S},
           /*         10,10*/ {S, S, S, S, S, S, S, S, S, S}};
        int myCardValue = myHand.getCard(0).value(); // Since myHand is a pair, we only need the value from one card.  
        int upCardValue = upCard.value();
        
        return section4Table[myCardValue-1][upCardValue-1];
    }
    
    /**
     * Recommends a Play from the 3rd section of the basic strategy table.
     * @param myHand The Player's hand
     * @param upCard The Dealer's upCard
     * @return A value of Play from the 3rd section.
     */
    public static Play section3(Hand myHand, Card upCard) {       
        Play[][] section3Table = {
                   // upCard = A  2  3  4  5  6  7  8  9 10
           /* myHand =  A,2*/ {H, H, H, H, D, D, H, H, H, H},
           /*           A,3*/ {H, H, H, H, D, D, H, H, H, H},
           /*           A,4*/ {H, H, H, D, D, D, H, H, H, H},
           /*           A,5*/ {H, H, H, D, D, D, H, H, H, H},
           /*           A,6*/ {H, H, D, D, D, D, H, H, H, H},
           /*           A,7*/ {H, S, D, D, D, D, S, S, H, H},
           /*        A,8-10*/ {S, S, S, S, S, S, S, S, S, S}};
        int myCardValue;
        if(myHand.getCard(0).isAce()) // We know one card in myHand is an Ace, but we need to find out which one.
            myCardValue = myHand.getCard(1).value(); // First card is the Ace, so get the value of the 2nd card.
        else
            myCardValue = myHand.getCard(0).value(); // Second card is the Ace, so get the value of the 1st card.
        
        if(myCardValue > 8)
            myCardValue = 8; // Since 8-10 have the same Plays, we will use 8 as the value for 9 and 10
        
        int upCardValue = upCard.value();
        
        return section3Table[myCardValue-2][upCardValue-1];
    }
    
    /**
     * Recommends a Play from the 2nd section of the basic strategy table.
     * @param myHand The Player's hand
     * @param upCard The Dealer's upCard
     * @return A value of Play from the 2nd section.
     */
    public static Play section2(Hand myHand, Card upCard) { 
        
        // If there are more than 2 cards in myHand, we can't DOUBLE_DOWN, so we always advise to HIT.
        if(myHand.size() > 2)
            return Play.HIT;
        else {
            Play[][] section2Table = {
                       // upCard = A  2  3  4  5  6  7  8  9 10
               /* myHand =    8*/ {H, H, H, H, H, H, H, H, H, H},
               /*             9*/ {H, H, D, D, D, D, H, H, H, H},
               /*            10*/ {H, D, D, D, D, D, D, D, D, H},
               /*            11*/ {H, D, D, D, D, D, D, D, D, D}};

            // We don't care about the individual cards, just the total value of the hand
            int myHandValue = myHand.getValue();

            // Since 5-8 have the same Plays, will use 8 as the value for 5, 6, and 7.
            if(myHandValue < 8)
                myHandValue = 8;

            int upCardValue = upCard.value();

            return section2Table[myHandValue-8][upCardValue-1];
        }
    }
    
    /**
     * Recommends a Play from the 1st section of the basic strategy table.
     * @param myHand The Player's hand
     * @param upCard The Dealer's upCard
     * @return A value of Play from the 1st section.
     */
    public static Play section1(Hand myHand, Card upCard) {       
        Play[][] section1Table = {
                   // upCard = A  2  3  4  5  6  7  8  9 10
           /* myHand =   12*/ {H, H, H, S, S, S, H, H, H, H},
           /*            13*/ {H, S, S, S, S, S, H, H, H, H},
           /*            14*/ {H, S, S, S, S, S, H, H, H, H},
           /*            15*/ {H, S, S, S, S, S, H, H, H, H},
           /*            16*/ {H, S, S, S, S, S, H, H, H, H},
           /*           17+*/ {S, S, S, S, S, S, S, S, S, S}};
        int myHandValue = myHand.getValue(); // We don't care about the individual cards, just the total value of the hand            
        int upCardValue = upCard.value();
        
        // myHand value of 17-20 all stay, so we will just use 17 as the index.
        if (myHandValue > 17)
            myHandValue = 17;
        
        return section1Table[myHandValue-12][upCardValue-1];
    }
}
