package com.jlbc.bouncingfigureskotlin

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceView
import kotlin.collections.ArrayList
import kotlin.random.Random

/*
 *  This class represents the specialized view the program uses
 *  as board to draw and animate.
 *
 *  https://developer.android.com/reference/android/view/SurfaceView
 *  https://developer.android.com/reference/android/view/SurfaceHolder
 *
 *  PROPERTIES:
 *      dimensions
 *
 *  METHODS
 *      onTouchEvent (override)
 *      run (override)
 *      resume
 *      pause
 */
class Board(context: Context) : SurfaceView(context), Runnable {

    // <editor-fold desc="INTERNAL FIELDS">

    // Objects for graphics methods
    private var surfaceHolder = holder
    private lateinit var canvas: Canvas
    // Variables related to threads
    private var thread: Thread? = null
    @Volatile private var drawing = false
    // Collection for figures
    private val figures = ArrayList<Figure>()

    // </editor-fold>

    // <editor-fold desc="PUBLIC PROPERTIES">

    var dimensions = RectF(0f, 0f, 0f, 0f)

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method is responsible for processing when the user
    // touches the screen.
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_DOWN) {
            // Creates equal randomly figures
            when (Random.nextInt(3)) {
                // Creates a circle
                0 -> figures.add(Circle(PointF(event.x, event.y)))
                // Creates a square
                1 -> figures.add(Square(PointF(event.x, event.y)))
                // Creates a triangle
                2 -> figures.add(Triangle(PointF(event.x, event.y)))
            }
        }
        return true
    }

    // This method is responsible for the continuous update of the
    // drawing board. Every cycle repetition updates and draws all
    // figures.
    override fun run() {
        while (drawing) {
            update()
            draw()
        }
    }

    // When activity resumes, this view also resumes,
    // starting the thread and letting the live drawing
    // cycle to occurs.
    fun resume() {
        drawing = true
        thread = Thread(this)
        thread!!.start()
    }

    // When activity pauses, this view also pauses,
    // stopping the thread and the live drawing cycle.
    fun pause() {
        drawing = false
        thread?.join()
    }

    // </editor-fold>

    // <editor-fold desc="PRIVATE METHODS">

    // This method updates all figures.
    private fun update() {
        // NOTE: This "copy" of the ArrayList is needed to avoid
        // ConcurrentModification error. This error can happen if
        // this cycle is executing and user tries to add another figure.
        for (figure in ArrayList(figures)) {
            figure.update(dimensions)
        }
    }

    // This method is the main method to draw a frame in
    // any given moment.
    private fun draw() {
        if (surfaceHolder.surface.isValid) {
            // Gets a canvas object
            canvas = surfaceHolder.lockCanvas()
            // Sets background color, text color and size
            canvas.drawColor(Color.argb(255, 0, 0, 0))
            // Draw all figures
            // NOTE: This "copy" of the ArrayList is needed to avoid
            // ConcurrentModification error. This error can happen if
            // this cycle is executing and user tries to add another figure.
            for (figure in ArrayList(figures)) {
                figure.draw(canvas)
            }
            // Updates the view with the canvas
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    // </editor-fold>
}