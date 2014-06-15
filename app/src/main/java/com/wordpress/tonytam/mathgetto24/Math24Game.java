package com.wordpress.tonytam.mathgetto24;

import android.util.Log;
import android.widget.ImageButton;

import com.wordpress.tonytam.model.cards.PlayingCardEasy24;
import com.wordpress.tonytam.model.cards.PlayingCardMedium24;
import com.wordpress.tonytam.model.cards.PlayingCardNoFace;
import com.wordpress.tonytam.model.cards.PlayingDeck;
import com.wordpress.tonytam.model.cards.PlayingCard;

import java.lang.reflect.Array;


/**
 * Created by tonytam on 6/4/14.
 */
public class Math24Game {
    public PlayingDeck deck;

    public PlayingDeck
            _easyDeck,
            _hardDeck,
            _mediumDeck;

    public int currentGameTime;

    public int
            player1Score,
            player2Score;

    public PlayingCard []hand;

    Math24Game () {
    }

    public void dealHand() {

        for (int i = 0; i <= 3; i++) {
            hand[i] = this.deck.drawRandomCard();
            Log.d("Math24Game: card ", hand[i].description());
        }
    }
    public void startGame() {

        this.currentGameTime = 600;
        this.player1Score = 0;
        this.player2Score = 0;

        if (this._hardDeck == null) {
            this._hardDeck = new PlayingDeck   (new PlayingCardNoFace());
        }

        if (this._easyDeck == null) {
            this._easyDeck = new PlayingDeck (new PlayingCardEasy24());
        }

        if (this._mediumDeck == null) {
            this._mediumDeck = new PlayingDeck (new PlayingCardMedium24());
        }
        this.deck = this._easyDeck;

        this.hand = new PlayingCard[4];
        this.dealHand();

        /*

        // The valid operators
        this.plusSel  = @selector(decimalNumberByAdding:);
        this.minusSel = @selector(decimalNumberBySubtracting:);
        this.mulSel   = @selector(decimalNumberByMultiplyingBy:);
        this.divSel   = @selector(decimalNumberByDividingBy:);

        // We will need to loop through the operators/selectors
        this.selectors = malloc(sizeof(SEL) * 4);
        this.selectors[0] = this.plusSel;
        this.selectors[1] = this.minusSel;
        this.selectors[2] = this.mulSel;
        this.selectors[3] = this.divSel;

        this.answerOperators = malloc(sizeof(SEL) * 3);
        this.operatorStrings = [[NSMutableArray alloc] init];
        this.answerCardArray = [[NSMutableArray alloc] init];
        this.answerArray = [[NSMutableArray alloc] init];

        // just because it's hard to map SEL, we have the string representations
        this.operatorChars = [NSArray arrayWithObjects:
        @"+",
        @"-",
        @"×",
        @"÷",
                nil];


        // Keep track of the UIButtons where the operators are
        this.operatorLabels2 = [NSArray arrayWithObjects:
        this.buttonPlus2,
                this.buttonMinus2,
                this.buttonMultiplication2,
                this.buttonDivision2,
                nil
        ];


        // Deal a new deck of cards

        // Keep track of the labels used to display the status/answers to both players
        this.labelAnswers = [[NSArray alloc] initWithObjects:this.labelAnswer, this.labelAnswer2, nil];


        // Start off with no answer controllers
        [self showAnswerControllers:FALSE];
        this.labelMiddleInfo.hidden = TRUE;
        */
    }
}
