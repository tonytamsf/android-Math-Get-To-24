package com.wordpress.tonytam.model.cards;

/**
 * @author tonytam  on 6/1/14.
 */
public class PlayingCard {
    public String suit;
    public int rank;
    public String strRank;
    private static String[] anArrayOfSuits = {
            "♠︎",
            "♣︎",
            "♥︎",
            "♦︎"
    };

    private String[] anArrayOfRanks = {
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
    static public String[] validSuits () {

        return anArrayOfSuits;
    }

     public String[] rankStrings() {

        return anArrayOfRanks;
        
    }

     public int maxRank() {

        return (this.rankStrings()).length;
    }

    @Override
    public String toString() {
        return this.description();
    }

    public String description() {

        return this.suit + " " + this.rank;
    }
}
