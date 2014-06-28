package com.wordpress.tonytam.model.cards;

/**
 * @author tonytam
 */
public class PlayingCardEasy24 extends PlayingCard {
    private String[] anArrayOfStrings = {
            "1",
            "3",
            "4",
            "6",
            "8"
    };
    @Override
    public String[] rankStrings() {

        return anArrayOfStrings;
    }
}
