package com.wordpress.tonytam.mathgetto24;

import com.wordpress.tonytam.mathgetto24.util.SystemUiHider;
import com.wordpress.tonytam.model.cards.PlayingCard;
import com.wordpress.tonytam.util.*;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.lang.reflect.Method;


import java.util.ArrayList;


// TODO swipe to re-deal card
// http://bit.ly/android-code-swipe-gesture-detection

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class Math24 extends Activity implements SwipeInterface {
    private static String TAG = "Math24";
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private ArrayList<String> answerOperatorStrings;
    private ArrayList<Method> answerOperators;

    private int numAnswerOperators = 0;
    /**
     * List of cards selected
     */
    ArrayList<PlayingCard> answerCardArray;

    /**
     * This will have cards and operators
     */
    ArrayList<Object> answerArray;

    /**
     * Which player press the button
     */
    private int answerPlayer = 0;

    private Math24Game game;

    public ImageButton[] operators;
    public Button[] cards;

    public TextView
            player1Timer,
            player2Timer,
            player1Score,
            player2Score,
            player1TimeLabel,
            player2TimeLabel,
            player1ScoreLabel,
            player2ScoreLabel,
            labelAnswer1,
            labelAnswer2
    ;

    public ArrayList<TextView> labelAnswers;

    public int[] numberDrawables;

    Button numNW, numNE, numSE, numSW;

    RadioButton easyLevel, mediumLevel, hardLevel;

    ImageButton operatorPlus,
            operatorDivide,
            operatorMinus,
            operatorMultiply,
            soundToggle,
            player2Got24,
            player1Got24;

    View mainView;

    private class CardHand {
        public PlayingCard card;
        public View view;
    }

    ArrayList<CardHand> cardHandArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_math24);

        final View contentView = findViewById(R.id.fullscreen_content1);
        this.mainView = contentView;

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider.hide();

        // http://developer.android.com/training/system-ui/status.html#41
        // http://developer.android.com/training/system-ui/immersive.html
        View decorView = getWindow().getDecorView();

        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);

        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        this.setupViews().
                disableOperators(true).
                showAnswerControllers(false).
                startGame().
                refreshGameUI()
                ;
    }

    Math24 startGame() {
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        if (game == null) {
            game = new Math24Game();
        }
        game.startGame();
        return this;
    }

    Math24 setupViews () {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();

        display.getSize(size);
        int width = size.x;

        this.player1Timer = (TextView) findViewById(R.id.player1Timer);
        this.player2Timer = (TextView) findViewById(R.id.player2Timer);
        this.player1TimeLabel = (TextView) findViewById(R.id.player1TimerLabel);
        this.player2TimeLabel = (TextView) findViewById(R.id.player2TimerLabel);
        this.player1ScoreLabel = (TextView) findViewById(R.id.player1Label);
        this.player2ScoreLabel = (TextView) findViewById(R.id.player2Label);

        this.labelAnswer1 = (TextView) findViewById(R.id.labelAnswer1);
        this.labelAnswer2 = (TextView) findViewById(R.id.labelAnswer2);

        this.numNW = (Button) findViewById(R.id.numNW);
        this.numNE = (Button) findViewById(R.id.numNE);
        this.numSE = (Button) findViewById(R.id.numSE);
        this.numSW = (Button) findViewById(R.id.numSW);

        this.numNW.setHeight(width / 3);
        this.numNW.setWidth(width / 3);

        this.numNE.setWidth(width / 3);
        this.numNE.setHeight(width / 3);

        this.numSW.setWidth(width / 3);
        this.numSW.setHeight(width / 3);

        this.numSE.setHeight(width / 3);
        this.numSE.setWidth(width / 3);

        this.player1Score = (TextView) findViewById(R.id.player1Score);
        this.player2Score = (TextView) findViewById(R.id.player2Score);

        this.numberDrawables = new int[36];
        this.numberDrawables[1] = R.drawable.num_1;
        this.numberDrawables[2] = R.drawable.num_2;
        this.numberDrawables[3] = R.drawable.num_3;
        this.numberDrawables[4] = R.drawable.num_4;
        this.numberDrawables[5] = R.drawable.num_5;
        this.numberDrawables[6] = R.drawable.num_6;
        this.numberDrawables[7] = R.drawable.num_7;
        this.numberDrawables[8] = R.drawable.num_8;
        this.numberDrawables[9] = R.drawable.num_9;
        this.numberDrawables[10] = R.drawable.num_10;
        this.numberDrawables[11] = R.drawable.num_10;
        this.numberDrawables[12] = R.drawable.num_10;
        this.numberDrawables[13] = R.drawable.num_10;

        this.operatorPlus = (ImageButton) findViewById(R.id.operatorPlus);
        this.operatorMinus = (ImageButton) findViewById(R.id.operatorMinus);
        this.operatorMultiply = (ImageButton) findViewById(R.id.operatorMultiply);
        this.operatorDivide = (ImageButton) findViewById(R.id.operatorDivide);

        this.operators = new ImageButton[]{
                this.operatorPlus,
                this.operatorMinus,
                this.operatorMultiply,
                this.operatorDivide
        };

        this.cards = new Button[]{
                this.numSW,
                this.numSE,
                this.numNW,
                this.numNE
        };
        this.answerCardArray = new ArrayList<PlayingCard>();
        this.answerArray = new ArrayList<Object>();
        this.answerOperatorStrings = new ArrayList<String>();
        this.answerOperators = new ArrayList<Method>();
        this.cardHandArrayList = new ArrayList<CardHand>(4);
        this.labelAnswers = new ArrayList<TextView>();

        labelAnswers.add(this.labelAnswer1);
        labelAnswers.add(this.labelAnswer2);

        this.easyLevel = (RadioButton) findViewById(R.id.easyLevel);
        this.mediumLevel = (RadioButton) findViewById(R.id.mediumLevel);
        this.hardLevel = (RadioButton) findViewById(R.id.hardLevel);
        this.soundToggle = (ImageButton) findViewById(R.id.soundToggleButton);

        this.player1Got24 = (ImageButton) findViewById(R.id.player1Got24);
        this.player2Got24 = (ImageButton) findViewById(R.id.player2Got24);

        /**
         * Tagging it for later when the user touches a card or operator
         */
        this.operatorPlus.setTag(R.integer.operator_tag, (Integer) Math24Game.PLUS_OPERATOR);
        this.operatorMinus.setTag(R.integer.operator_tag, (Integer)  Math24Game.MINUS_OPERATOR);
        this.operatorMultiply.setTag(R.integer.operator_tag, (Integer)  Math24Game.MULTIPLY_OPERATOR);
        this.operatorDivide.setTag(R.integer.operator_tag, (Integer)  Math24Game.DIVIDE_OPERATOR);


        // Swipe
        ActivitySwipeDetector swipe = new ActivitySwipeDetector(this);
        RelativeLayout swipe_layout = (RelativeLayout) findViewById(R.id.overallLayout);
        swipe_layout.setOnTouchListener(swipe);
        return this;
    }

    public void player1GotAnswer(View view) {

        Log.d(TAG, "player1GotAnswer");
        this.answerPlayer = 0;
        showAnswerControllers(true);
    }

    public void player2GotAnswer(View view) {

        this.answerPlayer = 1;

        Log.d(TAG, "player2GotAnswer");
        showAnswerControllers(true);

    }

    public void cardsTouched(View view) {
        CardHand cardHand = (CardHand) view.getTag(R.integer.card_tag);

        this.answerCardArray.add(cardHand.card);
        this.answerArray.add(cardHand);

        this.disableOperators(false);
        this.disableCards(true);

        // Keep the players informed about what has been selected, both players need to know
        for (int i = 0; i < 2; i++) {
            TextView labelAnswer = this.labelAnswers.get(i);
            if (labelAnswer.getText().equals( getString (R.string.how_do_you_24))) {
                labelAnswer.setText("");
            }
            labelAnswer.setText(
                    labelAnswer.getText() +
                            String.valueOf(cardHand.card.rank)
            );
        }


        Log.d(TAG, "cardsTouched " + this.answerCardArray.toString());

        if (answerArray.size() == 7) {
            // TODO: current
            AnswerPackage potentialAnswer = this.game.calculateHand(
                    (PlayingCard []) answerCardArray.toArray(new PlayingCard[0]),
                    (Method []) answerOperators.toArray(new Method[0]),
                    (String []) answerOperatorStrings.toArray(new String[0])
                    );
            if (potentialAnswer != null) {
                Log.d("cardsTouched", potentialAnswer.toString());
            }
        }
        /*


    // We have 4 cards and 3 operators, we are done
    if ([self.answerArray count] == 7) {
        NSString * finalText = [[NSString alloc] init];

        AnswerPackage *answer = [self calculateHand:self.answerCardArray
                                  usingOperators:self.answerOperators
                               withOperatorChars:self.operatorStrings];
        if (answer != nil && [answer.answer compare:rightAnswer] == NSOrderedSame) {

            // TODO: Internationalize these strings
            DLog(@"Player got it right: %@", answer.stringAnswer);
            finalText = [NSString stringWithFormat:@"Yay, You Got 24!!\n %@", answer.stringAnswer];
            [self rightAnswer:self.answerPlayer];
        } else {
            finalText = [NSString stringWithFormat:@"Sorry, Get To 24 with: \n%@", self.storeAnswerPackage.stringAnswer];
            [self wrongAnswer:self.answerPlayer];
        }

        if (self.answerPlayer == 0) {
            [self.labelMiddleInfo setTransform:CGAffineTransformMakeRotation(-M_PI)];
        } else {
            [self.labelMiddleInfo setTransform:CGAffineTransformMakeRotation(0)];
        }
        self.labelMiddleInfo.text = finalText;
        self.labelMiddleInfo.hidden = FALSE;
        return;
    }

}


         */
    }

    public void operatorsTouched(View view) {
        this.answerArray.add(view);
        this.answerOperatorStrings.add(Math24Game.operatorString((Integer) view.getTag(R.integer.operator_tag)));
        this.answerOperators.add((Method) Math24Game.operatorMethod ((Integer) view.getTag(R.integer.operator_tag)));

        this.labelAnswer1.setText(
                this.labelAnswer1.getText() +
                Math24Game.operatorString((Integer) view.getTag(R.integer.operator_tag))
        );
/*
        // Show the players where we are
        //
        for (int i = 0; i < 2; i++) {
            UILabel * labelAnswer =[self.labelAnswers objectAtIndex:i];

            if ([labelAnswer.text compare:@ "(How can you get to 24?)"]==NSOrderedSame){
                labelAnswer.text = @ "";
            }

            labelAnswer.text =[NSString stringWithFormat:@ "%@ %@",
                    labelAnswer.text,
                    self.operatorChars[operator.tag]];

        }
*/
        Log.d(TAG, "operatorsTouched");

        // Enable cards, disable operators
        this.disableOperators(true);
        this.disableCards(false);
    }

    Math24 disableOperators(Boolean bDisabled) {
        for (ImageButton b : this.operators) {
            Log.d(TAG, "disableOperators " + b);
            b.setEnabled(!bDisabled);
            b.setAlpha(bDisabled ? 0.2f : 1.0f);
        }
        return this;
    }

    void disableCards(Boolean bDisabled) {
        for (Button card : this.cards) {
            CardHand cardHand = (CardHand) card.getTag(R.integer.card_tag);

            if (!bDisabled && this.answerArray.contains(cardHand)) {
                continue;
            }
            if (bDisabled && this.answerArray.contains(cardHand)) {
                Log.d(TAG, "found card " + card.toString());

                card.setAlpha(0.2f);
            } else {
                Log.d(TAG, "not found card " + card.toString());
                card.setAlpha(bDisabled ? 0.6f : 1.0f);
            }
            card.setEnabled(!bDisabled);

        }
    }

    /*
     * Show or hide cards
     * @param visibility View.INVISIBLE | View.VISIBLE
     */
    public void setCardVisibility(Boolean visible) {
        for (Button card : this.cards) {
            card.setAlpha(visible ? 1.0f : 0.4f);
        }
    }


    /*
     * Call this when the data model changes
     */
    public Math24 refreshGameUI() {
        cardHandArrayList.clear();
        for (int i = 0; i < 4; i++) {

            CardHand cardHand = new CardHand();
            cardHand.card = game.hand[i];
            cardHand.view = cards[i];
            cardHandArrayList.add(i, cardHand);

            cards[i].setTag(R.integer.card_tag, cardHandArrayList.get(i));
            cards[i].setBackgroundResource(this.numberDrawables[game.hand[i].rank]);
        }

        this.player1Timer.setText(String.valueOf(game.currentGameTime));
        this.player2Timer.setText(String.valueOf(game.currentGameTime));
        this.player1Score.setText(String.valueOf(game.player1Score));
        this.player2Score.setText(String.valueOf(game.player2Score));
        return this;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void bottom2top(View v) {
        Log.d("Math24", "bottom2top");
    }

    @Override
    public void left2right(View v) {

        dealHand();

    }

    @Override
    public void right2left(View v) {
        dealHand();

    }

    public void dealHand() {
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar);

        // mProgress.setMax(100);
        // mProgress.setVisibility(View.VISIBLE);

        //this.numSE.animate().translationX(0).withLayer();

        // TODO: may not need this because the game is already multi-threaded dealing cards
        // the local Thread used for count-down
        class DealTask extends AsyncTask<Math24, Integer, Long> {
            int i = 0;

            protected void onPreExecute() {
                i = 0;
                Math24.this.setCardVisibility(false);
            }

            protected Long doInBackground(Math24... o) {
                Math24 mathGame = o[0];
                mathGame.game.dealHand();
                return 1L;
            }

            protected void onProgressUpdate(Integer... progress) {

                // mProgress.setProgress(i++);
                Log.d("DealTask:onProgressUpdate", progress.toString());
            }

            protected void onPostExecute(Long result) {
                Math24.this.refreshGameUI();

                // mProgress.setVisibility(View.INVISIBLE);

                Math24.this.setCardVisibility(true);

            }
        }

        new DealTask().execute(this);

        this.answerPlayer = -1;
        this.showAnswerControllers(false);
        game.currentGameTime = 600;
        game.returnHand();
        answerArray.clear();
        answerCardArray.clear();
        answerOperators.clear();
        answerOperatorStrings.clear();
        numAnswerOperators = 0;

        /*


    self.labelMiddleInfo.hidden = TRUE;

    if (![self.timer isValid]) {
        // Start the countdown
        self.timer = [NSTimer scheduledTimerWithTimeInterval:1
                                                      target:self
                                                    selector:@selector(countdown)
                                                    userInfo:nil
                                                     repeats:YES];
        //[AudioUtil playSound:@"relaxing-short" :@"wav"];
    }
         */
    }


    @Override
    public void top2bottom(View v) {
        Log.d("Math24", "top2bottom");
    }

    public void showSegmentLevels(Boolean visible) {

        this.easyLevel.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        this.mediumLevel.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        this.hardLevel.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        this.soundToggle.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);

        Log.d(TAG, "showSegmentLevels");
    }

    public Math24 showAnswerControllers(Boolean visible) {
        int viewInvisible = visible ? View.INVISIBLE : View.VISIBLE;
        int viewVisible = visible ? View.VISIBLE : View.INVISIBLE;

        if (visible) {
            this.disableOperators(visible);

            this.disableCards(!visible);
        }
        this.showSegmentLevels(!visible);
        this.player1Timer.setVisibility(viewInvisible);
        this.player2Timer.setVisibility(viewInvisible);
        this.player1Score.setVisibility(viewInvisible);
        this.player2Score.setVisibility(viewInvisible);
        this.player2TimeLabel.setVisibility(viewInvisible);
        this.player1TimeLabel.setVisibility(viewInvisible);
        this.player1ScoreLabel.setVisibility(viewInvisible);
        this.player2ScoreLabel.setVisibility(viewInvisible);
        this.labelAnswer1.setVisibility(viewVisible);
        this.labelAnswer2.setVisibility(viewVisible);
        this.player1Got24.setVisibility(viewInvisible);
        this.player2Got24.setVisibility(viewInvisible);


/*

        self.labelBackground1.hidden = show;
        self.labelBackground2.hidden = show;

        [self.imageViewCenter setUserInteractionEnabled:!show];
        if (visible) {
            if (self.answerPlayer == 0) {
                self.labelAnswer.text = @"(How can you get to 24?)";
                self.labelAnswer2.text = @"";
            } else {
                self.labelAnswer.text = @"";
                self.labelAnswer2.text = @"(How can you get to 24?)";
            }
        }
        */
        return this;
    }
}