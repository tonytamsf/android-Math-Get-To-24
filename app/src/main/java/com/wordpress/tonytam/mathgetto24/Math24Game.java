package com.wordpress.tonytam.mathgetto24;

import android.util.Log;

import com.wordpress.tonytam.model.cards.PlayingCardEasy24;
import com.wordpress.tonytam.model.cards.PlayingCardMedium24;
import com.wordpress.tonytam.model.cards.PlayingCardNoFace;
import com.wordpress.tonytam.model.cards.PlayingDeck;
import com.wordpress.tonytam.model.cards.PlayingCard;
import com.wordpress.tonytam.util.Permute;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Iterator;


/**
 * @author by tonytam on 6/4/14.
 */
public class Math24Game {
    public PlayingDeck deck;

    public PlayingDeck
            _easyDeck,
            _hardDeck,
            _mediumDeck;

    public PlayingCard hand[];
    public PlayingCard nextHand[];

    AnswerPackage answer;
    AnswerPackage nextAnswer;

    public int currentGameTime;

    public int
            player1Score,
            player2Score;

    public Method selectors[];
    public String operatorChars[] = {
            "+",
            "-",
            "×",
            "÷",
    };

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

    public PlayingCard [] dealHand() {
        if (nextHand[0] == null) {
            for (int numHands = 0; numHands <= 50; numHands++) {
                for (int i = 0; i <= 3; i++) {
                    this.hand[i] = Math24Game.this.deck.drawRandomCard();
                    Log.d("Math24Game: card ", this.hand[i].description());
                }

                nextAnswer = this.calculateAnswer();
                if (nextAnswer != null) {
                    // TODO: Put the cards back into the hand
                    break;
                }
            }
        } else {
            hand = nextHand;
            answer = nextAnswer;
            Log.d("dealHand", "Shortcut deal");
        }

        class CalculateAnswer extends Thread {

            public void run() {
                for (int numHands = 0; numHands <= 50; numHands++) {
                    for (int i = 0; i <= 3; i++) {
                        Math24Game.this.nextHand[i] = Math24Game.this.deck.drawRandomCard();
                        Log.d("Math24Game: card ", Math24Game.this.nextHand[i].description());
                    }

                    nextAnswer = Math24Game.this.calculateAnswer();
                    if (nextAnswer != null) {
                        // TODO: Put the cards back into the hand
                        break;
                    }
                }
                if (nextAnswer == null) {
                    // TODO: we got a problem, no answer for any cards
                }
            }
        }
        new CalculateAnswer().start();
        return hand;
    }

    public AnswerPackage calculateAnswer () {
        Permute permute = new Permute(this.hand);
        AnswerPackage answer = null;
        for (Iterator i = permute; i.hasNext(); ) {
            PlayingCard [] a = (PlayingCard[]) i.next();
            Log.d("calculateAnswer", toString(a));
            answer = calculateHand(a);
            if (answer != null) {
                break;
            }
        }
        if (answer != null) {
            Log.d("calculateAnswer", answer.stringAnswer + "=" + answer.answer.toString());
        } else {
            Log.d("calculateAnswer", "NO ANSWER");
        }
        return answer;
    }

    // Using the cards in the given sequence and operators in the sequence
    // calculate whether there is an answer.  This can be used
    // to verify the human players answer
    //
    // RETURN the answerPackage or nil if 24 is not found
    //
    public AnswerPackage calculateHand (PlayingCard cards[],
                                        Method operators[],
                                        String operatorStrs[]) {
        BigDecimal rightAnswer = new BigDecimal(24.0);
        AnswerPackage storeAnswerPackage;

        storeAnswerPackage = calculateSimple (
                cards,
                operators,
                operatorStrs);
        if (storeAnswerPackage != null &&
                storeAnswerPackage.answer.equals(rightAnswer)) {
            return storeAnswerPackage;
        }

        storeAnswerPackage = calculateNested (
                cards,
                operators,
                operatorStrs);
        if (storeAnswerPackage != null &&
                storeAnswerPackage.answer.equals(rightAnswer)) {
            return storeAnswerPackage;
        }

        storeAnswerPackage = calculateGrouping (
                cards,
                operators,
                operatorStrs);
        if (storeAnswerPackage != null &&
                storeAnswerPackage.answer.equals(rightAnswer)) {
            return storeAnswerPackage;
        }

        storeAnswerPackage = calculateGroupingThree (
                cards,
                operators,
                operatorStrs);
        if (storeAnswerPackage != null &&
                storeAnswerPackage.answer.equals(rightAnswer)) {
            return storeAnswerPackage;
        }

        storeAnswerPackage = calculateGroupingOfTwo (
                cards,
                operators,
                operatorStrs);
        if (storeAnswerPackage != null &&
                storeAnswerPackage.answer.equals(rightAnswer)) {
            return storeAnswerPackage;
        }

        storeAnswerPackage = calculateGroupingSecond (
                cards,
                operators,
                operatorStrs);
        if (storeAnswerPackage != null &&
                storeAnswerPackage.answer.equals(rightAnswer)) {
            return storeAnswerPackage;
        }
        
        return null;
    }
    /*
     * Apply all the possible operators on the 4 cards, keeping them in the same order
     * Solve for these combination
     * ((a op b) op c) op d
     * (a op b) op (c op d)
     * a op (b op c) op d
     */
    public AnswerPackage calculateHand (PlayingCard cards[]) {
        BigDecimal rightAnswer = new BigDecimal(24.0);
        AnswerPackage storeAnswerPackage;

        for (int j = 0; j <= 3; ++j) {
            for (int k = 0; k <= 3; ++k) {
                for (int l = 0; l <= 3; ++l) {
                    Method currentOperators[] = {
                            this.selectors[j],
                            this.selectors[k],
                            this.selectors[l]
                    };

                    String currentOperatorChars[] = {
                            this.operatorChars[j],
                            this.operatorChars[k],
                            this.operatorChars[l]
                    };

                    storeAnswerPackage = calculateHand(
                            cards,
                            currentOperators,
                            currentOperatorChars);
                    if (storeAnswerPackage != null &&
                        storeAnswerPackage.answer.equals(rightAnswer)) {
                        return storeAnswerPackage;
                    }
                    /*
                    if ([storeAnswerPackage.answer compare:rightAnswer]==NSOrderedSame){
                        DLog( @ "answer %@", storeAnswerPackage.stringAnswer);

                        found = TRUE;
                        break;
                    }
                    */
                }
            }
        }
        return null;
    }

    // ((a op b) op c) op d
    public AnswerPackage calculateSimple (PlayingCard cards[],
                                          Method operators[],
                                          String operatorStrs[]) {
        BigDecimal subtotal ;
        AnswerPackage answer = new AnswerPackage();
        Method selector0 = operators[0];
        Method selector1 = operators[1];
        Method selector2 = operators[2];

        PlayingCard card0 = cards[0];
        PlayingCard card1 = cards[1];
        PlayingCard card2 = cards[2];
        PlayingCard card3 = cards[3];

        try {
            subtotal = (BigDecimal) selector0.invoke(new BigDecimal(card0.rank), new BigDecimal(card1.rank));

            subtotal = (BigDecimal) selector1.invoke(subtotal, new BigDecimal(card2.rank));

            subtotal = (BigDecimal) selector2.invoke(subtotal, new BigDecimal(card3.rank));
        } catch (InvocationTargetException e) {
            // e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            // e.printStackTrace();
            return null;
        }

        answer.answer = subtotal;
        answer.stringFormat = "((%d %s %d) %s %d) %s %d";
        answer.stringAnswer = String.format(answer.stringFormat,
                card0.rank, operatorStrs[0],
                card1.rank, operatorStrs[1],
                card2.rank, operatorStrs[2],
                card3.rank);

        //Log.i("calculateSimple", answer.stringAnswer);
        //Log.i("calculateSimple", answer.answer.toString());

        return answer;
    }

    // (a op b) op (c op d)
    public AnswerPackage calculateGrouping(PlayingCard cards[],
                                           Method operators[],
                                           String operatorStrs[]) {
        BigDecimal subtotal, subtotal1;
        AnswerPackage answer = new AnswerPackage();
        Method selector0 = operators[0];
        Method selector1 = operators[1];
        Method selector2 = operators[2];

        PlayingCard card0 = cards[0];
        PlayingCard card1 = cards[1];
        PlayingCard card2 = cards[2];
        PlayingCard card3 = cards[3];

        try {
            subtotal = (BigDecimal) selector0.invoke(new BigDecimal(card0.rank), new BigDecimal(card1.rank));

            subtotal1 = (BigDecimal) selector2.invoke(new BigDecimal(card2.rank), new BigDecimal(card3.rank));

            subtotal = (BigDecimal) selector1.invoke(subtotal, subtotal1);
        } catch (InvocationTargetException e) {
            // e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            // e.printStackTrace();
            return null;
        }

        answer.answer = subtotal;
        answer.stringFormat = "(%d %s %d) %s (%d %s %d)";
        answer.stringAnswer = String.format(answer.stringFormat,
                card0.rank, operatorStrs[0],
                card1.rank, operatorStrs[1],
                card2.rank, operatorStrs[2],
                card3.rank);

        //Log.i("calculateSimple", answer.stringAnswer);
        //Log.i("calculateSimple", answer.answer.toString());

        return answer;
    }

    // a op (b op (c op d))
    public AnswerPackage calculateGroupingThree(PlayingCard cards[],
                                           Method operators[],
                                           String operatorStrs[]) {
        BigDecimal subtotal;
        AnswerPackage answer = new AnswerPackage();
        Method selector0 = operators[0];
        Method selector1 = operators[1];
        Method selector2 = operators[2];

        PlayingCard card0 = cards[0];
        PlayingCard card1 = cards[1];
        PlayingCard card2 = cards[2];
        PlayingCard card3 = cards[3];

        try {
            subtotal = (BigDecimal) selector2.invoke(new BigDecimal(card2.rank), new BigDecimal(card3.rank));

            subtotal = (BigDecimal) selector1.invoke(new BigDecimal(card1.rank), subtotal);

            subtotal = (BigDecimal) selector0.invoke(new BigDecimal(card0.rank), subtotal);
        } catch (InvocationTargetException e) {
            // e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            // e.printStackTrace();
            return null;
        }

        answer.answer = subtotal;
        answer.stringFormat = "%d %s ((%d %s (%d %s %d))";
        answer.stringAnswer = String.format(answer.stringFormat,
                card0.rank, operatorStrs[0],
                card1.rank, operatorStrs[1],
                card2.rank, operatorStrs[2],
                card3.rank);

        //Log.i("calculateSimple", answer.stringAnswer);
        //Log.i("calculateSimple", answer.answer.toString());

        return answer;
    }


    // (a op b) op c op d
    public AnswerPackage calculateGroupingOfTwo  (PlayingCard cards[],
                                         Method operators[],
                                         String operatorStrs[]) {
        BigDecimal subtotal;
        AnswerPackage answer = new AnswerPackage();
        Method selector0 = operators[0];
        Method selector1 = operators[1];
        Method selector2 = operators[2];

        PlayingCard card0 = cards[0];
        PlayingCard card1 = cards[1];
        PlayingCard card2 = cards[2];
        PlayingCard card3 = cards[3];

        try {
            subtotal = (BigDecimal) selector0.invoke(new BigDecimal(card0.rank), new BigDecimal(card1.rank));

            subtotal = (BigDecimal) selector1.invoke(subtotal, new BigDecimal(card2.rank));

            subtotal = (BigDecimal) selector2.invoke(subtotal, new BigDecimal(card3.rank));
        } catch (InvocationTargetException e) {
            // e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            // e.printStackTrace();
            return null;
        }

        answer.answer = subtotal;
        answer.stringFormat = "(%d %s %d) %s %d %s %d";
        answer.stringAnswer = String.format(answer.stringFormat,
                card0.rank, operatorStrs[0],
                card1.rank, operatorStrs[1],
                card2.rank, operatorStrs[2],
                card3.rank);

        //Log.i("calculateSimple", answer.stringAnswer);
        //Log.i("calculateSimple", answer.answer.toString());

        return answer;
    }

    // a op ((b op c) op d)
    public AnswerPackage calculateNested(PlayingCard cards[],
                                         Method operators[],
                                         String operatorStrs[]) {
        BigDecimal subtotal;
        AnswerPackage answer = new AnswerPackage();
        Method selector0 = operators[0];
        Method selector1 = operators[1];
        Method selector2 = operators[2];

        PlayingCard card0 = cards[0];
        PlayingCard card1 = cards[1];
        PlayingCard card2 = cards[2];
        PlayingCard card3 = cards[3];

        try {
            subtotal = (BigDecimal) selector1.invoke(new BigDecimal(card1.rank), new BigDecimal(card2.rank));

            subtotal = (BigDecimal) selector2.invoke(subtotal, new BigDecimal(card3.rank));

            subtotal = (BigDecimal) selector0.invoke(new BigDecimal(card0.rank), subtotal);
        } catch (InvocationTargetException e) {
            // e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            // e.printStackTrace();
            return null;
        }

        answer.answer = subtotal;
        answer.stringFormat = "%d %s ((%d %s %d) %s %d)";
        answer.stringAnswer = String.format(answer.stringFormat,
                card0.rank, operatorStrs[0],
                card1.rank, operatorStrs[1],
                card2.rank, operatorStrs[2],
                card3.rank);

        //Log.i("calculateSimple", answer.stringAnswer);
        //Log.i("calculateSimple", answer.answer.toString());

        return answer;
    }

    // a op (b op c) op d
    public AnswerPackage calculateGroupingSecond (PlayingCard cards[],
                                         Method operators[],
                                         String operatorStrs[]) {
        BigDecimal subtotal;
        AnswerPackage answer = new AnswerPackage();
        Method selector0 = operators[0];
        Method selector1 = operators[1];
        Method selector2 = operators[2];

        PlayingCard card0 = cards[0];
        PlayingCard card1 = cards[1];
        PlayingCard card2 = cards[2];
        PlayingCard card3 = cards[3];

        try {
            subtotal = (BigDecimal) selector1.invoke(new BigDecimal(card1.rank), new BigDecimal(card2.rank));

            subtotal = (BigDecimal) selector0.invoke(new BigDecimal(card0.rank), subtotal);

            subtotal = (BigDecimal) selector2.invoke(subtotal, new BigDecimal(card3.rank));
        } catch (InvocationTargetException e) {
            // e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            // e.printStackTrace();
            return null;
        }

        answer.answer = subtotal;
        answer.stringFormat = "%d %s (%d %s %d) %s %d";
        answer.stringAnswer = String.format(answer.stringFormat,
                card0.rank, operatorStrs[0],
                card1.rank, operatorStrs[1],
                card2.rank, operatorStrs[2],
                card3.rank);

        //Log.i("calculateSimple", answer.stringAnswer);
        //Log.i("calculateSimple", answer.answer.toString());

        return answer;
    }

    public Math24Game returnHand () {
        for (PlayingCard card : hand) {
            deck.addCard(card);

        }

        for (int i = 0; i < hand.length; i++) {
            hand[i] = null;
        }
        return this;
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
            this._hardDeck = new PlayingDeck (new PlayingCardNoFace());
        }

        if (this._easyDeck == null) {
            this._easyDeck = new PlayingDeck (new PlayingCardEasy24());
        }

        if (this._mediumDeck == null) {
            this._mediumDeck = new PlayingDeck (new PlayingCardMedium24());
        }
        this.deck = this._hardDeck;

        this.hand = new PlayingCard[4];
        this.nextHand = new PlayingCard[4];
        nextAnswer = new AnswerPackage();
        answer = new AnswerPackage();

        this.dealHand();

        // TODO handle operators in a method array
        /*


        this.answerOperators = malloc(sizeof(SEL) * 3);
        this.operatorStrings = [[NSMutableArray alloc] init];
        this.answerCardArray = [[NSMutableArray alloc] init];
        this.answerArray = [[NSMutableArray alloc] init];

        // just because it's hard to map SEL, we have the string representations

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
