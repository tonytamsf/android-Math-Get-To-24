package com.wordpress.tonytam.model.cards;

/**
 * @author tonytam
 */
public class PlayingCardMedium24 extends PlayingCard {
    private String[] anArrayOfStrings = {
            "1",
            "3",
            "4",
            "6",
            "8",
            "10"
    };
    @Override
     public String[] rankStrings() {

        return anArrayOfStrings;

    }
}
