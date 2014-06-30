package com.wordpress.tonytam.mathgetto24;

import com.wordpress.tonytam.mathgetto24.util.SystemUiHider;
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
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Objects;


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
    private ArrayList<String> answerOperators;

    /**
     * List of cards selected
     * TODO: really shouldn't be the list of buttons
     */
    ArrayList<Button> answerCardArray;

    /**
     * This will have cards and operators
     */
    ArrayList<Object> answerArray;

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

    public int[] numberDrawables;

    Button numNW, numNE, numSE, numSW;

    RadioButton easyLevel, mediumLevel, hardLevel;
    ImageButton soundToggle;

    ImageButton operatorPlus,
            operatorDivide,
            operatorMinus,
            operatorMultiply;

    View mainView;

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
        this.answerCardArray = new ArrayList<Button>();
        this.answerArray = new ArrayList<Object>();
        this.answerOperatorStrings = new ArrayList<String>();
        this.answerOperators = new ArrayList<String>();

        this.easyLevel = (RadioButton) findViewById(R.id.easyLevel);
        this.mediumLevel = (RadioButton) findViewById(R.id.mediumLevel);
        this.hardLevel = (RadioButton) findViewById(R.id.hardLevel);
        this.soundToggle = (ImageButton) findViewById(R.id.soundToggleButton);

        // Swipe
        ActivitySwipeDetector swipe = new ActivitySwipeDetector(this);
        RelativeLayout swipe_layout = (RelativeLayout) findViewById(R.id.overallLayout);
        swipe_layout.setOnTouchListener(swipe);
        return this;
    }
    public void player1GotAnswer(View view) {

        Log.d(TAG, "player1GotAnswer");
        showAnswerControllers(true);
    }

    public void player2GotAnswer(View view) {

        Log.d(TAG, "player2GotAnswer");
        showAnswerControllers(true);

    }

    public void cardsTouched(View view) {
        this.answerCardArray.add((Button) view);
        this.disableOperators(false);
        this.disableCards(true);

        Log.d(TAG, "cardsTouched " + this.answerCardArray.toString());
    }

    public void operatorsTouched(View view) {
        this.answerArray.add(view);
        this.answerOperatorStrings.add((String) view.getTag());
        this.answerOperators.add((String) view.getTag());
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
            //card.setEnabled(! bDisabled);

            if (!bDisabled && this.answerCardArray.contains(card)) {
                continue;
            }
            if (bDisabled && this.answerCardArray.contains(card)) {
                Log.d(TAG, "found card " + card.toString());

                card.setAlpha(0.2f);
            } else {
                Log.d(TAG, "not found card " + card.toString());

                card.setAlpha(bDisabled ? 0.6f : 1.0f);
            }
        }
    }

    /*
     * Show or hide cards
     * @param visibility View.INVISIBLE | View.VISIBLE
     */
    public void setCardVisbility(Boolean visible) {
        for (Button card : this.cards) {
            card.setAlpha(visible ? 1.0f : 0.4f);
        }
    }


    /*
     * Call this when the data model changes
     */
    public Math24 refreshGameUI() {
        this.numNW.setBackgroundResource(this.numberDrawables[game.hand[0].rank]);
        this.numNE.setBackgroundResource(this.numberDrawables[game.hand[1].rank]);
        this.numSW.setBackgroundResource(this.numberDrawables[game.hand[2].rank]);
        this.numSE.setBackgroundResource(this.numberDrawables[game.hand[3].rank]);

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

        // the local Thread used for count-down
        class DealTask extends AsyncTask<Math24, Integer, Long> {
            int i = 0;

            protected void onPreExecute() {
                i = 0;
                Math24.this.setCardVisbility(false);
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

                Math24.this.setCardVisbility(true);

            }
        }

        new DealTask().execute(this);
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

/*
        // Show the area where the answers are shown
        self.labelAnswer.hidden = !show;
        self.labelAnswer2.hidden = !show;

        self.player1Button.hidden = show;
        self.player2Button.hidden = show;

        self.labelTime.hidden = show;
        self.labelTime1.hidden = show;
        self.labelTimeStatic.hidden = show;
        self.labelTimeStatic1.hidden = show;
        self.player2NameLabel.hidden = show;
        self.player1NameLabel.hidden = show;
        self.player1Score.hidden = show;
        self.player2Score.hidden = show;
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