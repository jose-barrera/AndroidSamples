package com.jlbc.livedrawingkotlin

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics

class MainActivity : Activity() {

    private lateinit var liveDrawingView: LiveDrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Calculates the available screen size
        // NOTE: getDefaultDisplay is deprecated in API Level 30,
        //       we use for compatibility reasons.
        val display = windowManager.defaultDisplay
        // DisplayMetrics class offers more information than getSize()
        val displayMetrics = DisplayMetrics()
        display.getRealMetrics(displayMetrics)

        // Creates and sets the object that will manage the real-time
        // drawing process
        liveDrawingView = LiveDrawingView(this,
            displayMetrics.widthPixels,
            displayMetrics.heightPixels)
        setContentView(liveDrawingView)
    }

    override fun onResume() {
        super.onResume()
        // When activity resumes, also the
        // LiveDrawingView resumes.
        liveDrawingView.resume()
    }

    override fun onPause() {
        super.onPause()
        // When activity pauses, also the
        // LiveDrawingView pauses.
        liveDrawingView.pause()
    }
}

/*
 *  Reference: Horton (2018). Android Programming for Beginners
 */