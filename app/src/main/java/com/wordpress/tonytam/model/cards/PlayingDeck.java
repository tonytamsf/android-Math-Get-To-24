package com.wordpress.tonytam.model.cards;

/**
 * @author tonytam
 */
public class PlayingDeck extends Deck {

    public PlayingDeck (PlayingCard prototypeCard) {
        super();
        init(prototypeCard);
    }
    // build a new playing deck of cards
    public PlayingDeck init (PlayingCard prototypeCard) {
        String [] suits = PlayingCard.validSuits();
        Class c = prototypeCard.getClass();
        for (String suit : suits) {
            for (int rank = 1; rank <= prototypeCard.maxRank(); rank++) {
                PlayingCard card = null;
                try {
                     card = (PlayingCard) c.newInstance();
                } catch ( IllegalAccessException e) {
                    // Whatever dude
                } catch ( InstantiationException e ) {
                    // Whatever dude
                }
                card.rank = rank;
                card.strRank = card.rankStrings()[rank - 1];
                card.suit = suit;
                this.addCard(card);
            }
        }
        return this;
    }
}

