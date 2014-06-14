package com.wordpress.tonytam.model.cards;

/**
 * Created by tonytam on 6/1/14.
 */
public class PlayingCard {
    public String suit;
    public int rank;
    public String strRank;

    static public String[] validSuits () {
        String[] anArrayOfStrings = {
                "♠︎",
                "♣︎",
                "♥︎",
                "♦︎"
        };
        return anArrayOfStrings;
    }

    static public String[] rankStrings() {
        String[] anArrayOfStrings = {
                "A",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "J",
                "Q",
                "K"
        };
        return anArrayOfStrings;
        
    }

    static public int maxRank() {
        return (PlayingCard.rankStrings()).length;
    }

    public String description() {
        return this.suit + " " + this.rank;
    }
}
