package com.wordpress.tonytam.model.cards;

/**
 * @author tonytam
 */
public class PlayingCardNoFace extends PlayingCard {
    private String[] anArrayOfStrings = {
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10"
    };
    @Override
     public String[] rankStrings() {

        return anArrayOfStrings;
    }
}
