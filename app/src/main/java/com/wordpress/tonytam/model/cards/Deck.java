package com.wordpress.tonytam.model.cards;

import java.util.*;

/**
 * @author tonytam
 */
public class Deck <T> {
    private ArrayList cards ;

    public Deck() {
        cards = new ArrayList();
    }

    public void addCard (T card) {
        cards.add(card);

    }

    // TODO: without this, cards are empty
    public void dealNewDeck() {

    }
    public PlayingCard drawRandomCard () {
        PlayingCard randomCard = null;
        Random rand = new Random();

        if (this.cards.size() > 0) {
            int index = java.lang.Math.abs(rand.nextInt() % this.cards.size());
            randomCard =  (PlayingCard) this.cards.get(index);
            //Log.d("PlayingCard", randomCard.description());
            //this.cards.remove(index);
        }

        return randomCard;
    }
}