package com.wordpress.tonytam.mathgetto24;

import com.wordpress.tonytam.mathgetto24.util.SystemUiHider;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class Math24 extends Activity {
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
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private Math24Game game;

    public TextView
            player1Timer,
            player2Timer,
            player1Score,
            player2Score;

    public int [] numberDrawables;

    Button numNW, numNE, numSE, numSW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_math24);

        final View contentView = findViewById (R.id.fullscreen_content1);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider.hide();

        // http://developer.android.com/training/system-ui/status.html#41
        // http://developer.android.com/training/system-ui/immersive.html
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE ;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        if (game == null) {
            game = new Math24Game();
        }
        game.startGame();

        this.player1Timer = (TextView) findViewById(R.id.player1Timer);
        this.player2Timer =  (TextView) findViewById(R.id.player2Timer);

        this.numNW = (Button) findViewById(R.id.numNW);
        this.numNE = (Button) findViewById(R.id.numNE);
        this.numSE = (Button) findViewById(R.id.numSE);
        this.numSW = (Button) findViewById(R.id.numSW);
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
        refreshGameUI();
    }


    public void refreshGameUI ( ) {
        this.numNW.setBackgroundResource(this.numberDrawables[game.hand[0].rank]);
        this.numNE.setBackgroundResource(this.numberDrawables[game.hand[1].rank]);
        this.numSW.setBackgroundResource(this.numberDrawables[game.hand[2].rank]);
        this.numSE.setBackgroundResource(this.numberDrawables[game.hand[3].rank]);

        this.player1Timer.setText( String.valueOf( game.currentGameTime ));
        this.player2Timer.setText( String.valueOf( game.currentGameTime ));
        this.player1Score.setText( String.valueOf( game.player1Score ));
        this.player2Score.setText( String.valueOf( game.player2Score ));
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
}
