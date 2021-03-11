package charlie.bot.server;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.client.BasicStrategy;
import charlie.dealer.Dealer;
import charlie.dealer.Seat;
import charlie.plugin.IBot;
import charlie.util.Play;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 * This class simulates a player at a Blackjack table.
 * The bot follows the basic strategy for all of its
 * plays.
 * @author brendan
 */
public class RightBot implements IBot, Runnable{
    protected final int MAX_THINKING = 3;
    protected Dealer dealer;
    protected Hand botHand;
    protected Seat botSeat;
    protected Hid botHid;
    protected Card upCard;
    protected Random ran = new Random();
    protected boolean isBotTurn = false;
    private final Logger LOG = Logger.getLogger(RightBot.class);
    private final String botName = this.getClass().getName();

    @Override
    /**
     * Gets the bot's hand.
     * 
     * @return the bot's hand
     */
    public Hand getHand() {
        return botHand;
    }

    @Override
    /**
     * Sets the dealer for this bot
     * 
     * @param dealer the dealer
     */
    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    @Override
    /**
     * When the bot is sat by the dealer, it creates
     * a new Hid with the given seat and creates its
     * hand with that Hid.
     * 
     * @param seat the bot's seat
     */
    public void sit(Seat seat) {
        botSeat = seat;
        
        // Create new Hid now that we have the bot's seat
        botHid = new Hid(seat);
        
        // Create the bot's hand with the botHid
        botHand = new Hand(botHid);
        
        LOG.info("Seating " + botName + " at seat: " + seat);
    }

    @Override
    /**
     * Writes a line to the log indicating the game is starting
     * 
     * @param hids all hids in the current game
     * @param shoeSize the current size of the Shoe
     */
    public void startGame(List<Hid> hids, int shoeSize) {
        LOG.info("Starting new game");
    }

    @Override
    /**
     * Writes a line to the log indicating the game is over
     * 
     * @param shoesize the current size of the Shoe
     */
    public void endGame(int shoeSize) {
        LOG.info("Game over. Shoe size = "+shoeSize);
    }

    @Override
    /**
     * The bot checks for 2 things when the dealer invokes deal:
     *      1. If the card belongs to the dealer and we don't have
     *         the upCard yet, we save the card as the upCard.
     *      2. If it is still the bot's turn, we invoke play.
     * 
     * @param hid the hid of the hand that was dealt a card
     * @param card the card that was dealt
     * @param values the values of each hand
     */
    public void deal(Hid hid, Card card, int[] values) {
        
        // If the Hid is the dealer's and upCard is null, this card must be the upCard
        if(hid.getSeat().equals(Seat.DEALER)  && upCard == null)
            upCard = card;
        
        // If it is still the bot's turn, then it must play
        else if(isBotTurn)
            play(hid);
    }

    @Override
    /**
     * Not implemented since insurance is not support by Charlie.
     */
    public void insure() {
    }

    @Override
    /**
     * Writes line to the log if it is this bot who busted.
     * 
     * @param hid the hid of the hand that busted
     */
    public void bust(Hid hid) {
        if(hid.equals(botHid))
            LOG.info(botName + " has busted");
    }

    @Override
    /**
     * Writes line to the log if it is this bot who won.
     * 
     * @param hid the hid of the hand that won
     */
    public void win(Hid hid) {
        if(hid.equals(botHid))
            LOG.info(botName + " won the hand");
    }

    @Override
    /**
     * Writes line to the log if it is this bot who got Blackjack.
     * 
     * @param hid the hid of the hand that got Blackjack
     */
    public void blackjack(Hid hid) {
        if(hid.equals(botHid))
            LOG.info(botName + " got Blackjack");
    }

    @Override
    /**
     * Writes line to the log if it is this bot who got a Charlie.
     * 
     * @param hid the hid of the hand that got a Charlie
     */
    public void charlie(Hid hid) {
        if(hid.equals(botHid))
            LOG.info(botName + " got a Charlie");
    }

    @Override
    /**
     * Writes line to the log if it is this bot who lost.
     * 
     * @param hid the hid of the hand that lost
     */
    public void lose(Hid hid) {
        if(hid.equals(botHid))
            LOG.info(botName + " lost the hand");
    }

    @Override
    /**
     * Writes line to the log if it is this bot who pushed.
     * 
     * @param hid the hid of the hand that pushed
     */
    public void push(Hid hid) {
        if(hid.equals(botHid))
            LOG.info(botName + " pushed");
    }

    @Override
    /**
     * Not implemented since shuffling does not matter to this bot.
     */
    public void shuffling() {
    }

    @Override
    /**
     * If it is the bot's turn, it spawns a worker thread
     * to calculate its play.
     * 
     * @param hid the hid of the hand that is playing
     */
    public void play(Hid hid) {
        
        // If the seat for hid is this bot's seat, then it is the bot's turn
        isBotTurn = hid.getSeat() == botSeat;
        
        // Spawn the worker thread if its the bot's turn
        if(isBotTurn) {
            LOG.info(botName + " starting turn");
            new Thread(this).start();
        }
    }

    @Override
    /**
     * Not implemented since bots should never split.
     */
    public void split(Hid newHid, Hid origHid) {
    }

    @Override
    /**
     * The worker thread that calculates the bots play
     * and sends it to the dealer.
     */
    public void run() {
        try{
            // This bot simulates a human player, so it takes time to "think" about its play
            int thinking = 1000 + ran.nextInt(MAX_THINKING * 1000);
            Thread.sleep(thinking);
            
            // Get the play from the BasicStrategy
            Play play = BasicStrategy.getPlay(botHand, upCard);
            
            // Since bots cannot split, the play must be corrected
            if(play.equals(Play.SPLIT))
                play = correct();
            
            synchronized(dealer) {
                switch(play) {
                    case HIT:
                        LOG.info(botName + " hitting");
                        
                        // Tell the dealer we want to hit
                        dealer.hit(this,botHid);
                        break;
                        
                    case STAY:
                        LOG.info(botName + " staying");
                        
                        // After staying, it won't be the bot's turn
                        isBotTurn = false;
                        
                        // Tell the dealer we want to stay
                        dealer.stay(this, botHid);
                        break;
                        
                    case DOUBLE_DOWN:
                        LOG.info(botName + " doubling down");
                        
                        // After doubling down, it won't be the bot's turn
                        isBotTurn = false;
                        
                        // Tell the dealer we want to double down
                        dealer.doubleDown(this, botHid);
                        break;
                }
            }        
        }
        catch(InterruptedException e){   
            return;
        }
    }
    
    /**
     * Corrects splits since bots cannot split.
     * 
     * @return the corrected play from BasicStrategy
     */
    private Play correct() {
        if(botHand.getValue() < 12)
            return BasicStrategy.section2(botHand, upCard);
        else
            return BasicStrategy.section1(botHand, upCard);
    }
    
}
