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

package charlie.sidebet.view;

import static charlie.audio.Effect.CHIPS_IN;
import static charlie.audio.Effect.CHIPS_OUT;
import charlie.audio.SoundFactory;
import charlie.card.Hid;
import charlie.plugin.ISideBetView;
import charlie.view.AMoneyManager;
import charlie.view.sprite.Chip;
import charlie.view.sprite.ChipButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 * This class implements the side bet view
 * @author Ron Coleman, Ph.D. and Brendan Van Allen
 */
public class SideBetView implements ISideBetView {
    private final Logger LOG = Logger.getLogger(SideBetView.class);
    
    public final static int X = 400;
    public final static int Y = 200;
    public final static int DIAMETER = 50;
    
    protected Font font = new Font("Arial", Font.BOLD, 18);
    protected BasicStroke stroke = new BasicStroke(3);
    
    // See http://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
    protected float dash1[] = {10.0f};
    protected BasicStroke dashed
            = new BasicStroke(3.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1, 0.0f);   

    protected List<ChipButton> buttons;
    protected int amt = 0;
    protected AMoneyManager moneyManager;
    
    protected List<Chip> chipsPressed = new ArrayList<>();
    protected Random r = new Random();
    protected int offsetX = 30;
    protected int offsetY = -20;
    
    protected boolean gameOver = false;
    protected String outcomeText;
    protected Color looseColorBg = new Color(250,58,5);
    protected Color looseColorFg = Color.WHITE;
    protected Color winColorFg = Color.BLACK;
    protected Color winColorBg = new Color(116,255,4);

    /**
     * Constructor for the SideBetView.
     */
    public SideBetView() {
        LOG.info("side bet view constructed");
    }
    
    /**
     * Sets the money manager.
     * @param moneyManager The moneyManager for Charlie
     */
    @Override
    public void setMoneyManager(AMoneyManager moneyManager) {
        this.moneyManager = moneyManager;
        this.buttons = moneyManager.getButtons();
    }
    
    /**
     * Registers a click for the side bet.
     * @param x X coordinate
     * @param y Y coordinate
     */
    @Override
    public void click(int x, int y) {
        int oldAmt = amt;
        
        // Test if any chip button has been pressed.
        for(ChipButton button: buttons) {
            if(button.isPressed(x, y)) {
                chipsPressed.add(new Chip(button.getImage(),X+offsetX,Y+offsetY,button.getAmt()));
                
                // Add to the offsets to simulate the chips being 'randomly' thrown on the table
                offsetX += 5 + r.nextInt(20);
                offsetY = -30 + r.nextInt(20);
                
                amt += button.getAmt();
                SoundFactory.play(CHIPS_IN);
                LOG.info("A. side bet amount "+button.getAmt()+" updated new amt = "+amt);
            }       
        }
        
        // Test if the at-stake area was pressed. If pressed, the sidebet gets cleared.
        if(x < X+DIAMETER/2 && x > X-DIAMETER/2 && y < Y+DIAMETER/2 && y > Y-DIAMETER/2) {
            amt = 0;
            SoundFactory.play(CHIPS_OUT);
            chipsPressed.clear();
            offsetX = 30;
            offsetY = -20;
            LOG.info("B. side bet amount cleared");
        }
    }

    /**
     * Informs view the game is over and it's time to update the bankroll for the hand.
     * @param hid Hand id
     */
    @Override
    public void ending(Hid hid) {
        double bet = hid.getSideAmt();
        
        if(bet == 0)
            return;
        
        else if(bet > 0)
            outcomeText = "WIN!";
        
        else
            outcomeText = "LOSE!";
        
        gameOver = true;

        LOG.info("side bet outcome = "+bet);
        
        // Update the bankroll
        moneyManager.increase(bet);
        
        LOG.info("new bankroll = "+moneyManager.getBankroll());
    }

    /**
     * Informs view the game is starting
     */
    @Override
    public void starting() {
        gameOver = false;
    }

    /**
     * Gets the side bet amount.
     * @return Bet amount
     */
    @Override
    public Integer getAmt() {
        return amt;
    }

    /**
     * Updates the view
     */
    @Override
    public void update() {
    }

    /**
     * Renders the view
     * @param g Graphics context
     */
    @Override
    public void render(Graphics2D g) {
        // Draw the at-stake place on the table
        g.setColor(Color.RED); 
        g.setStroke(dashed);
        g.drawOval(X-DIAMETER/2, Y-DIAMETER/2, DIAMETER, DIAMETER);
        
        // Draw the at-stake amount
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(""+amt, X-5, Y+5);
        
        // Draw the payout amounts for each side bet
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("SUPER 7 pays 3:1", X+100, Y-90);
        g.drawString("ROYAL MATCH pays 25:1", X+100, Y-70);
        g.drawString("EXACTLY 13 pays 1:1", X+100, Y-50);
        
        // Draw the chips using randomness
        for(Chip chip : chipsPressed) {
            chip.render(g);
        }
        
        // If the game is over and a bet was made, draw the outcome
        if(gameOver && amt != 0) {
            Font outcomeFont = new Font("Arial", Font.BOLD, 18);
            FontMetrics fm = g.getFontMetrics(outcomeFont);
            int w = fm.charsWidth(outcomeText.toCharArray(), 0, outcomeText.length());
            int h = fm.getHeight();
            
            // Paint the outcome background            
            if (outcomeText.equals("LOSE!"))
                g.setColor(looseColorBg);
            else
                g.setColor(winColorBg);    
            
            g.fillRoundRect(X+40, Y-h-25, w, h, 5, 5);
            
            // Paint the outcome foreground            
            if (outcomeText.equals("LOSE!"))
                g.setColor(looseColorFg);
            else
                g.setColor(winColorFg);    
            
            // Draw the outcome text
            g.setFont(outcomeFont);
            g.drawString(outcomeText,X+40,Y-30);
        }
        
    }
}
