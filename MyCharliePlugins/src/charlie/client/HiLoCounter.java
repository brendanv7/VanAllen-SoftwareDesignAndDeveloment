package charlie.client;

import charlie.card.Card;
import charlie.plugin.ICardCounter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * A simple implementation of the Hi-Lo card counting
 * system for BlackJack. This class implements the ICardCounter
 * plugin for Charlie.
 * 
 * @author Brendan Van Allen
 */
public class HiLoCounter implements ICardCounter {
    
    protected int runningCount = 0;
    protected double decksInShoe = 0.0;
    protected double trueCount = 0;
    protected int betAmt = 1;
    protected boolean shufflePending = false;
    
    protected Font font = new Font("Arial", Font.BOLD, 16);
    public final static int X = 500;
    public final static int Y = 250;

    @Override
    /**
     * Tells the HiLoCounter that a new game is starting.
     * 
     * @param shoeSize The number of cards in the shoe
     */
    public void startGame(int shoeSize) {
        // shoeSize gives use the number of cards in the shoe, so we must convert this
        decksInShoe = Math.rint(shoeSize / 52.0);
        
        // If the shoe has less than 32 cards, the rint method will round down to 0, so we must fix that
        if(decksInShoe == 0.0)
            decksInShoe = 1.0;
        
        // Check if we need reset our counts because of a shuffle
        if(shufflePending) {
            runningCount = 0;
            trueCount = 0;
            shufflePending = false;
        }
    }

    @Override
    /**
     * Updates HiLoCounter's members to reflect the most
     * current game state. This method is called whenever
     * a card is dealt from the Shoe.
     * 
     * @param card The last card dealt from the Shoe
     */
    public void update(Card card) {
        
        // 10, J, Q, K, and A all count as -1.
        if(card.getRank() >= 10 || card.isAce())
            runningCount--;
        
        // 2-6 count as +1.
        else if(card.getRank() >= 2 && card.getRank() <= 6)
            runningCount++;
        
        trueCount = runningCount / decksInShoe;
        betAmt = (int) Math.max(1, 1 + trueCount);
    }

    @Override
    /**
     * Renders the Hi-Lo card counting information.
     * 
     * @param g Graphics context referencing the casino table.
     */
    public void render(Graphics2D g) {
        // Draw Hi-Lo
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Hi-Lo", X, Y);
        g.drawString("Shoe size: " + decksInShoe, X, Y+20);
        
        if(runningCount > 0) {
            g.setColor(Color.GREEN);
            g.drawString("Running count: +" + runningCount, X, Y+40);
        }
        else if(runningCount < 0) { 
            g.setColor(Color.RED);
            g.drawString("Running count: " + runningCount, X, Y+40);
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Running count: " + runningCount, X, Y+40);
        }
        
        
        if(trueCount > 0) {
            g.setColor(Color.GREEN);
            g.drawString("True count: +" + trueCount, X, Y+60);
        }
        else if (trueCount < 0) {
            g.setColor(Color.RED);
            g.drawString("True count: " + trueCount, X, Y+60);
        } else {
            g.setColor(Color.WHITE);
            g.drawString("True count: " + trueCount, X, Y+60);
        }
        
        g.setColor(Color.WHITE);
        g.drawString("Bet: " + betAmt + " chips", X, Y+80);
        
    }

    @Override
    /**
     * Notifies this CardCounter that the Shoe will be
     * shuffled after the current game.
     */
    public void shufflePending() {
        shufflePending = true;
    }
    
}
