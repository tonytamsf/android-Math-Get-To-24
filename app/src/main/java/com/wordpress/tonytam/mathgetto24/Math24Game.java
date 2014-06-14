package com.wordpress.tonytam.mathgetto24;

import com.wordpress.tonytam.model.cards.Deck;

/**
 * Created by tonytam on 6/4/14.
 */
public class Math24Game {
    public Deck deck;

    Math24Game () {
        deck = new Deck();
    }

    public void startGame() {
        deck.drawRandomCard();
    }
}
