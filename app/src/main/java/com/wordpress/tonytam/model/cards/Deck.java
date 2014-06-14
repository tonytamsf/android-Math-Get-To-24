package com.wordpress.tonytam.model.cards;

import android.util.Log;

import java.util.*;

/**
 * Created by tonytam on 6/4/14.
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
    public T drawRandomCard () {
        T randomCard = null;
        Random rand = new Random();

        if (this.cards.size() > 0) {
            int index = rand.nextInt() % this.cards.size();
            randomCard = (T) this.cards.get(index);
            //Log.d("playingcard", randomCard.description());
            this.cards.remove(index);
        }

        return randomCard;
    }
}