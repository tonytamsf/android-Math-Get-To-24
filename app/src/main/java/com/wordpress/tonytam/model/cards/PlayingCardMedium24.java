package com.wordpress.tonytam.model.cards;

/**
 * Created by tonytam on 6/4/14.
 */
public class PlayingCardMedium24 extends PlayingCard {
    @Override
     public String[] rankStrings() {
        String[] anArrayOfStrings = {
                "1",
                "3",
                "4",
                "6",
                "8",
                "10"
        };
        return anArrayOfStrings;

    }
}
