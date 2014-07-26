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
import java.util.ArrayList;
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

    static final public int LEVEL_EASY = 1;
    static final public int LEVEL_MEDIUM = 2;
    static final public int LEVEL_HARD = 3;

    static final public int PLUS_OPERATOR = 0;
    static final public int MINUS_OPERATOR = 1;
    static final public int MULTIPLY_OPERATOR = 2;
    static final public int DIVIDE_OPERATOR = 3;

    static public BigDecimal rightAnswer;

    public PlayingCard hand[];
    public PlayingCard nextHand[];

    AnswerPackage thisAnswer;
    AnswerPackage nextAnswer;

    public int currentGameTime;

    public ArrayList<Integer> playerScores;


    static public Method operators[];

    static public String operatorChars[] = {
            "+",
            "-",
            "×",
            "÷",
    };

    Math24Game () {
        Class bigDecimalClass = BigDecimal.class;
        operators = new Method[4];
        try {
            operators[0] = bigDecimalClass.getMethod("add", BigDecimal.class);
            operators[1] = bigDecimalClass.getMethod("subtract", BigDecimal.class);
            operators[2] = bigDecimalClass.getMethod("multiply", BigDecimal.class);
            operators[3] = bigDecimalClass.getMethod("divide", BigDecimal.class);
        } catch (NoSuchMethodException e) {
            Log.e("Math24Game", e.toString());
        }
    }

    static public BigDecimal getRightAnswer () {
        if (Math24Game.rightAnswer == null) {
            Math24Game.rightAnswer = new BigDecimal(24);
        }
        return rightAnswer;
    }
    static public String operatorString(int op) {
        return operatorChars[op];
    }

    static public Method operatorMethod(int op) {
        return operators[op];
    }

    static public Method methodOf (int op ) {
        return operators[op];
    }

    public PlayingCard [] dealHand() {
        if (nextHand[0] == null) {
            for (int numHands = 0; numHands <= 50; numHands++) {
                for (int i = 0; i <= 3; i++) {
                    this.hand[i] = Math24Game.this.deck.drawRandomCard();
                    Log.d("Math24Game: dealHand ", this.hand[i].description());
                }

                thisAnswer = this.calculateAnswer();
                if (thisAnswer != null) {
                    Log.d("dealHand", "deal next hand");
                    break;
                } else {
                    Log.d("dealHand", "try again for another hand");
                    returnHand();
                }
            }
        } else {
            hand = nextHand.clone();
            thisAnswer = nextAnswer;
            Log.d("dealHand", "Shortcut deal");
        }

        class CalculateAnswer extends Thread {

            public void run() {
                for (int numHands = 0; numHands <= 50; numHands++) {
                    for (int i = 0; i <= 3; i++) {
                        Math24Game.this.nextHand[i] = Math24Game.this.deck.drawRandomCard();
                        Log.d("Math24Game: card ", Math24Game.this.nextHand[i].description());
                    }

                    nextAnswer = Math24Game.this.calculateAnswer(Math24Game.this.nextHand);
                    if (nextAnswer != null) {
                        break;
                    } else {
                        // Next hand should be returned
                        returnHand(Math24Game.this.nextHand);
                    }
                }
                if (nextAnswer == null) {
                    Log.d("dealHand", "No winning hand");
                }
            }
        }
        new CalculateAnswer().start();
        return hand;
    }

    public AnswerPackage calculateAnswer (PlayingCard hand[]) {
        Permute permute = new Permute(hand);
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

    public AnswerPackage calculateAnswer () {

        return calculateAnswer(this.hand);
    }

    public void setLevel (int level) {
        Log.d("setLevel", String.valueOf(level));
        switch (level) {
            case LEVEL_EASY:
                deck = _easyDeck;
                break;
            case LEVEL_MEDIUM:
                deck = _mediumDeck;
                break;
            case LEVEL_HARD:
                deck = _hardDeck;
                break;
            default:
                Log.e("setLevel", "Invalid level " + String.valueOf(level));
                break;
        }
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
        BigDecimal rightAnswer = getRightAnswer();
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

        if (storeAnswerPackage != null ) {
            Log.d("CalculateHand", storeAnswerPackage.toString());
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
        BigDecimal rightAnswer = getRightAnswer();
        AnswerPackage storeAnswerPackage;

        for (int j = 0; j <= 3; ++j) {
            for (int k = 0; k <= 3; ++k) {
                for (int l = 0; l <= 3; ++l) {
                    Method currentOperators[] = {
                            this.operators[j],
                            this.operators[k],
                            this.operators[l]
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
        return returnHand(this.hand);
    }

    public Math24Game returnHand (PlayingCard hand[]) {
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

    // validate playerNumber to be 0 or 1
    public Integer getPlayerScore(int playerNumber) {
            return playerScores.get(playerNumber);
    }
    public Integer playerRightAnswer(int playerNumber) {
        this.playerScores.set(playerNumber,
                playerScores.get(playerNumber).intValue() + 1);
        return playerScores.get(playerNumber);
    }

    public Integer playerWrongAnswer (int playerNumber) {
        this.playerScores.set(playerNumber,
                playerScores.get(playerNumber).intValue() - 1);
        return playerScores.get(playerNumber);
    }

    public void startGame() {

        this.currentGameTime = 600;
        this.playerScores = new ArrayList<Integer>(2);
        this.playerScores.add(new Integer(0));
        this.playerScores.add(new Integer(0));

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
        thisAnswer = new AnswerPackage();

        this.dealHand();

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
