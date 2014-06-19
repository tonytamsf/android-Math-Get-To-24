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

     public String[] rankStrings() {
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
