package com.jlbc.bouncingfiguresjava;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;

public class MainActivity extends Activity {

    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Calculates the available screen size
        // NOTE: getDefaultDisplay is deprecated in API Level 30,
        //       we use for compatibility reasons.
        Display display = getWindowManager().getDefaultDisplay();
        // DisplayMetrics class offers more information than getSize()
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        // Creates and sets the object that will manage the real-time
        // drawing process
        board = new Board(this);
        board.setDimensions(0f, 0f, displayMetrics.widthPixels, displayMetrics.heightPixels);

        setContentView(board);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // When activity resumes, also the
        // Board resumes.
        board.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // When activity pauses, also the
        // Board pauses.
        board.pause();
    }
}