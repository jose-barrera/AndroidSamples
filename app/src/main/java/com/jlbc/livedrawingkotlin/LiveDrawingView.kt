package com.jlbc.livedrawingkotlin

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceView

/*
 *  This class represents the specialized view the program uses
 *  to draw and animate.
 *
 *  https://developer.android.com/reference/android/view/SurfaceView
 *  https://developer.android.com/reference/android/view/SurfaceHolder
 *
 *  PROPERTIES:
 *      n/a
 *
 *  METHODS
 *      onTouchEvent (override)
 *      run (override)
 *      pause
 *      resume
 */
class LiveDrawingView(context: Context, screenX: Int, screenY: Int) : SurfaceView(context), Runnable  {

    // <editor-fold desc="COMPANION OBJECT">

    companion object {
        private const val DEBUGGING = true
        private const val MILLIS_IN_SECOND = 1000
        private const val MAX_SYSTEMS = 1000
        private const val PARTICLES_PER_SYSTEM = 100
    }

    // </editor-fold>

    // <editor-fold desc="INTERNAL FIELDS">

    // Objects for graphics methods
    private val surfaceHolder = holder
    private lateinit var canvas: Canvas
    private val paint = Paint()
    // Internal fields related to sizes
    private var fps: Long = 0
    private val fontSize = screenX / 20f
    private val fontMargin = screenX / 75f
    // Variables related to threads
    private var thread: Thread? = null
    @Volatile private var drawing = false
    private var paused = true
    // Objects for the buttons
    private val resetButton = RectF(0f, 0f, 100f, 100f)
    private val togglePauseButton = RectF(0f, 150f, 100f, 250f)
    // Collection and index for particle systems
    private val particleSystems = ArrayList<ParticleSystem>()
    private var nextSystem = 0

    // </editor-fold>

    // <editor-fold desc="INITIALIZER BLOCKS">

    init {
        // Sets all particle systems
        for (i in 1..MAX_SYSTEMS) {
            val particleSystem = ParticleSystem()
            particleSystem.init(PARTICLES_PER_SYSTEM)
            particleSystems.add(particleSystem)
        }
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method is responsible for processing when the user
    // touches the screen.
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Checks if the user is moving his/her finger
        // while keeps touching the screen
        if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
            // Gets the position from touch
            val position = PointF(event.x, event.y)
            // Sets particle system's emission
            particleSystems[nextSystem].emit(position)
            // Sets next system validating a circular shifting
            nextSystem++
            if (nextSystem == MAX_SYSTEMS) {
                nextSystem = 0
            }
        }

        // Checks if the user touches a button (like a click)
        if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            // Verifies if position is inside reset button
            if (resetButton.contains(event.x, event.y)) {
                nextSystem = 0
                paused = true
                pause()
                particleSystems.forEach {
                    it.reset(PARTICLES_PER_SYSTEM)
                }
                resume()
            }
            // Verifies if position is inside toggle pause button
            if (togglePauseButton.contains(event.x, event.y)) {
                paused = !paused
            }
        }

        return true
    }

    // This method is responsible for the continuous update of the
    // drawing view. Every cycle repetition calculates the elapsed
    // elapsed time took to update all drawing, and with this time
    // calculates the actual frames per second (fps) instant rate.
    override fun run() {
        while (drawing) {
            // Records starting time
            val frameStartTime = System.currentTimeMillis()
            if (!paused) {
                update()
            }
            draw()
            // Calculates the elapsed time
            val timeThisFrame = System.currentTimeMillis() - frameStartTime
            // Calculates the instant fps
            if (timeThisFrame > 0) {
                fps = MILLIS_IN_SECOND / timeThisFrame
            }
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

    // When activity pauses, this view also pauses.
    // stopping the thread and the live drawing cycle.
    fun pause() {
        drawing = false
        thread?.join()
    }

    // </editor-fold>

    // <editor-fold desc="PRIVATE METHODS">

    // This method updates all particle systems (only
    // those who are running).
    private fun update() {
        particleSystems.forEach {
            if (it.running) {
                it.update(fps)
            }
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
            paint.color = Color.argb(255, 255, 255, 255)
            paint.textSize = fontSize
            if (DEBUGGING) {
                printDebuggingText()
            }
            // Sets color for the buttons and draw them
            paint.color = Color.argb(255, 255, 0, 255)
            canvas.drawRoundRect(resetButton, 20f, 20f, paint)
            canvas.drawRoundRect(togglePauseButton, 20f, 20f, paint)
            paint.color = Color.argb(255, 0, 0, 0)
            canvas.drawText("R", 35f, 65f, paint)
            if (paused) {
                canvas.drawText("S", 35f, 215f, paint)
            } else {
                canvas.drawText("P", 35f, 215f, paint)
            }
            // Draw all particles systems
            particleSystems.forEach {
                it.draw(canvas, paint)
            }
            // Updates the view with the canvas
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    // This methods prints debugging information into
    // the display.
    private fun printDebuggingText() {
        val debugSize = fontSize / 2f
        val debugStart = 450f
        paint.textSize = debugSize
        var visibleSystems = 0
        particleSystems.forEach {
            visibleSystems += (if (it.visible) 1 else 0)
        }
        canvas.drawText("FPS: $fps",
            10f, debugStart + debugSize, paint)
        canvas.drawText("Systems: $visibleSystems",
            10f, fontMargin + debugStart + debugSize * 2f, paint)
        canvas.drawText("Particles: ${visibleSystems * PARTICLES_PER_SYSTEM}",
            10f, fontMargin + debugStart + debugSize * 3f, paint)
    }

    // </editor-fold>
}

/*
 *  Reference: Horton (2018). Android Programming for Beginners
 */