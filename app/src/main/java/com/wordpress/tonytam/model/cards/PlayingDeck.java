package com.wordpress.tonytam.model.cards;

/**
 * Created by tonytam on 6/12/14.
 */
public class PlayingDeck extends Deck {
    // build a new playing deck of cards
    PlayingDeck init (PlayingCard prototypeCard) {
        String suit;
        String [] suits = PlayingCard.validSuits();
        Class c = prototypeCard.getClass();
        for (int i = 0; i < suits.length;  i++) {
            suit = suits[i];
            for (int rank = 1; rank <= PlayingCard.maxRank(); rank++) {
                PlayingCard card = null;
                try {
                     card = (PlayingCard) c.newInstance();
                } catch ( IllegalAccessException e) {
                    // Whatever dude
                } catch ( InstantiationException e ) {
                    // Whatever dude
                }
                card.rank = rank;
                card.strRank = card.rankStrings()[rank];
                card.suit = suit;
                this.addCard(card);
            }
        }
        return this;
    }
}

