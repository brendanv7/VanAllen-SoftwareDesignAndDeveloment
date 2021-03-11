package charlie.bot.server;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.dealer.Seat;
import charlie.plugin.IBot;
import java.util.List;
import java.util.Random;

/**
 * A very basic implementation of the IBot interface of Charlie.
 * HueyStay is a bot that always stays no matter what.
 * @author Brendan Van Allen
 */
public class HueyStay implements IBot, Runnable {
    protected final int MAX_THINKING = 5;
    protected Seat mine;
    protected Hand myHand;
    protected Dealer dealer;
    protected Random ran = new Random();
    
    
    @Override
    public Hand getHand() {
        return myHand;
    }

    @Override
    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    @Override
    public void sit(Seat seat) {
        this.mine = seat;
        Hid hid = new Hid(mine);
        this.myHand = new Hand(hid);
    }

    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
    }

    @Override
    public void endGame(int shoeSize) {
    }

    @Override
    public void deal(Hid hid, Card card, int[] values) {
    }

    @Override
    public void insure() {
    }

    @Override
    public void bust(Hid hid) {
    }

    @Override
    public void win(Hid hid) {
    }

    @Override
    public void blackjack(Hid hid) {
    }

    @Override
    public void charlie(Hid hid) {
    }

    @Override
    public void lose(Hid hid) {
    }

    @Override
    public void push(Hid hid) {
    }

    @Override
    public void shuffling() {
    }

    @Override
    public void play(Hid hid) {
        if(hid.getSeat() != mine)
            return;
        
        new Thread(this).start();
    }

    @Override
    public void split(Hid newHid, Hid origHid) {
    }

    @Override
    public void run() {
        try{
            int thinking = ran.nextInt(MAX_THINKING * 1000);
            Thread.sleep(thinking);
            dealer.stay(this, myHand.getHid());
        }
        catch(InterruptedException e){
            
        }
    }
    
}
