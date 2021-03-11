/*
 Copyright (c) 2014 Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package charlie.sidebet.rule;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.ISideBetRule;
import org.apache.log4j.Logger;

/**
 * This class implements the side bet rule for Super 7, Royal Match
 * and Exactly 13.
 * @author Ron Coleman and Brendan Van Allen
 */
public class SideBetRule implements ISideBetRule {
    private final Logger LOG = Logger.getLogger(SideBetRule.class);
    
    private final Double PAYOFF_SUPER7 = 3.0;
    private final Double PAYOFF_ROYAL_MATCH = 25.0;
    private final Double PAYOFF_EXACTLY13 = 1.0;

    /**
     * Apply rule to the hand and return the payout if the rule matches
     * and the negative bet if the rule does not match.
     * @param hand Hand to analyze.
     * @return Payout amount: <0 lose, >0 win, =0 no bet
     */
    @Override
    public double apply(Hand hand) {
        Double bet = hand.getHid().getSideAmt();
        LOG.info("side bet amount = "+bet);
        
        // No bet
        if(bet == 0)
            return 0.0;
        
        LOG.info("side bet rule applying hand = "+hand);
        
        Card card1 = hand.getCard(0);
        Card card2 = hand.getCard(1);
        
        // Royal Match has highest payout, so check that first
        boolean isRoyal = (card1.getRank() == Card.KING && card2.getRank() == Card.QUEEN) ||
                          (card1.getRank() == Card.QUEEN && card2.getRank() == Card.KING);
        boolean isMatch = card1.getSuit().equals(card2.getSuit());
        
        if(isRoyal && isMatch) {
            LOG.info("side bet ROYAL MATCH matches");
            return bet * PAYOFF_ROYAL_MATCH;
        }
        
        // Super 7 has the next highest payout, so now check that        
        if(card1.getRank() == 7) {
            LOG.info("side bet SUPER 7 matches");
            return bet * PAYOFF_SUPER7;
        }
        
        // Exactly 13 has the lowest payout of the 3, so check that last
        int total = card1.value() + card2.value();
        if(total == 13){
            LOG.info("side bet EXACTLY 13 matches");
            return bet * PAYOFF_EXACTLY13;
        }
        
        LOG.info("side bet rule no match");
        
        // None of the side bets matched, so the bet was lost
        return -bet;
    }
}
