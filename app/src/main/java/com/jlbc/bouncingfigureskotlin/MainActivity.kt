package com.jlbc.bouncingfigureskotlin

import android.app.Activity
import android.graphics.RectF
import android.os.Bundle
import android.util.DisplayMetrics

class MainActivity : Activity() {

    private lateinit var board: Board

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Calculates the available screen size
        // NOTE: defaultDisplay is deprecated in API Level 30,
        //       we use for compatibility reasons.
        val display = windowManager.defaultDisplay
        // DisplayMetrics class offers more information than getSize()
        val displayMetrics = DisplayMetrics()
        display.getRealMetrics(displayMetrics)

        // Creates and sets the object that will manage the real-time
        // drawing process
        board = Board(this)
        board.dimensions = RectF(0f, 0f,
            displayMetrics.widthPixels.toFloat(), displayMetrics.heightPixels.toFloat())

        setContentView(board)
    }

    override fun onResume() {
        super.onResume()
        // When activity resumes, also the
        // Board resumes.
        board.resume()
    }

    override fun onPause() {
        super.onPause()
        // When activity pauses, also the
        // Board pauses.
        board.pause()
    }
}