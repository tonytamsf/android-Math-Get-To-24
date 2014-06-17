package com.wordpress.tonytam.mathgetto24;

import android.util.Log;
import android.widget.ImageButton;

import com.wordpress.tonytam.mathgetto24.AnswerPackage;
import com.wordpress.tonytam.model.cards.PlayingCardEasy24;
import com.wordpress.tonytam.model.cards.PlayingCardMedium24;
import com.wordpress.tonytam.model.cards.PlayingCardNoFace;
import com.wordpress.tonytam.model.cards.PlayingDeck;
import com.wordpress.tonytam.model.cards.PlayingCard;
import com.wordpress.tonytam.util.Permute;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Created by tonytam on 6/4/14.
 */
public class Math24Game {
    public PlayingDeck deck;

    public PlayingDeck
            _easyDeck,
            _hardDeck,
            _mediumDeck;

    public PlayingCard hand[];

    public int currentGameTime;

    public int
            player1Score,
            player2Score;

    public Method selectors[];

    Math24Game () {
        Class bigDecimalClass = BigDecimal.class;
        selectors = new Method[4];
        try {
            selectors[0] = bigDecimalClass.getMethod("add", BigDecimal.class);
            selectors[1] = bigDecimalClass.getMethod("subtract", BigDecimal.class);
            selectors[2] = bigDecimalClass.getMethod("multiply", BigDecimal.class);
            selectors[3] = bigDecimalClass.getMethod("divide", BigDecimal.class);
        } catch (NoSuchMethodException e) {
            Log.e("Math24Game", e.toString());
        }
    }

    public void dealHand() {

        for (int i = 0; i <= 3; i++) {
            hand[i] = this.deck.drawRandomCard();
            Log.d("Math24Game: card ", hand[i].description());
        }
        this.calculateAnswer();
    }

    public void calculateAnswer () {
        Permute permute = new Permute(this.hand);
        for (Iterator i = permute; i.hasNext(); ) {
            PlayingCard [] a = (PlayingCard[]) i.next();
            Log.d("Math24Game", toString(a));
        }
    }

    /*
     * Apply all the possible operators on the 4 cards, keeping them in the same order
     * Solve for these combination
     * ((a op b) op c) op d
     * (a op b) op (c op d)
     * a op (b op c) op d
     */

    public AnswerPackage calculateHand (PlayingCard hand) {
        Boolean found = false;
        BigDecimal rightAnswer = new BigDecimal(24.0);
        AnswerPackage storeAnswerPackage = null;

        for (int j = 0; j <= 3; ++j) {
            for (int k = 0; k <= 3; ++k) {
                for (int l = 0; l <= 3; ++l) {
                    Method currentOperators[] = {
                            this.selectors[j],
                            this.selectors[k],
                            this.selectors[l]
                    };
                }
            }
        }

        if (found) {
            return storeAnswerPackage;
        } else {
            return null;
        }
    }

    public String toString(PlayingCard []ar) {
        final int n = Array.getLength(ar);
        final StringBuffer sb = new StringBuffer("[");
        for (int j = 0; j < n; j++) {
            sb.append(Array.get(ar, j).toString());
            if (j < n - 1) sb.append(",");
        }
        sb.append("]");
        return new String(sb);
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

        // TODO handle operators in a method array
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



        // Keep track of the labels used to display the status/answers to both players
        this.labelAnswers = [[NSArray alloc] initWithObjects:this.labelAnswer, this.labelAnswer2, nil];

        // Start off with no answer controllers
        [self showAnswerControllers:FALSE];
        this.labelMiddleInfo.hidden = TRUE;
        */
    }
}
