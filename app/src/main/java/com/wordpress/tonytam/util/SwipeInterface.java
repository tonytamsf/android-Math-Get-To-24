package com.wordpress.tonytam.util;
import android.view.View;

// http://stackoverflow.com/questions/937313/android-basic-gesture-detection

public interface SwipeInterface {

    public void bottom2top(View v);

    public void left2right(View v);

    public void right2left(View v);

    public void top2bottom(View v);

}