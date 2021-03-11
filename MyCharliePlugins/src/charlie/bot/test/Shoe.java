package charlie.bot.test;

import java.util.Random;

/**
 * This shoe is used to test the bot plugins.
 * @author Brendan Van Allen
 */
public class Shoe extends charlie.card.Shoe{

    @Override
    public void init( ) {
        ran = new Random(1);
        load();
        shuffle();
    }
}
