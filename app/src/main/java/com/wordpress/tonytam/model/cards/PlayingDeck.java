package com.wordpress.tonytam.model.cards;

/**
 * Created by tonytam on 6/12/14.
 */
public class PlayingDeck extends Deck {
    // build a new playing deck of cards
    PlayingDeck init (playingcard prototypeCard) {
        String suit;
        String [] suits = playingcard.validSuits();
        Class c = prototypeCard.getClass();
        for (int i = 0; i < suits.length;  i++) {
            suit = suits[i];
            for (int rank = 1; rank <= playingcard.maxRank(); rank++) {
                playingcard card = null;
                try {
                     card = (playingcard) c.newInstance();
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

