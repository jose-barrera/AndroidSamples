package com.jlbc.livedrawingjava;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

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
 *      resume
 *      pause
 */
public class LiveDrawingView extends SurfaceView implements Runnable {

    // <editor-fold desc="INTERNAL FIELDS">

    // Internal constants
    private final boolean DEBUGGING = true;
    private final int MILLIS_IN_SECOND = 1000;
    private final int MAX_SYSTEMS = 1000;
    private int PARTICLES_PER_SYSTEM = 100;
    // Object for graphics methods
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint paint;
    // Internal fields related to sizes
    private long fps;
    private int screenX;
    private int screenY;
    private float fontSize;
    private float fontMargin;
    // Variables related to threads
    private Thread thread = null;
    private volatile boolean drawing;
    private boolean paused = true;
    // Objects for the buttons
    private RectF resetButton;
    private RectF togglePauseButton;
    // Collection and index for particle systems
    private ArrayList<ParticleSystem> particleSystems = new ArrayList<>();
    private int nextSystem = 0;

    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">

    public LiveDrawingView(Context context, int x, int y) {
        super(context);
        // Stores internal object's data
        screenX = x;
        screenY = y;
        // Sets font properties
        fontSize = screenX / 20f;
        fontMargin = screenX / 75f;
        // Gets drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();
        // Defines the buttons
        resetButton = new RectF(0f,0f,100f,100f);
        togglePauseButton = new RectF(0f, 150f, 100f, 250f);
        // Sets all particle systems
        for (int i = 0; i < MAX_SYSTEMS; i++) {
            ParticleSystem particleSystem = new ParticleSystem();
            particleSystem.init(PARTICLES_PER_SYSTEM);
            particleSystems.add(particleSystem);
        }
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method is responsible for processing when the user
    // touches the screen.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Checks if the user is moving his/her finger
        // while keeps touching the screen
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
            // Gets the position from touch
            PointF position = new PointF(event.getX(), event.getY());
            // Sets particle system's emission
            particleSystems.get(nextSystem).emit(position);
            // Sets next system validating a circular shifting
            nextSystem++;
            if (nextSystem == MAX_SYSTEMS) {
                nextSystem = 0;
            }
        }

        // Checks if the user touches a button (like a click)
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            // Verifies if position is inside reset button
            if (resetButton.contains(event.getX(), event.getY())) {
                nextSystem = 0;
                paused = true;
                pause();
                for (ParticleSystem particleSystem : particleSystems) {
                    particleSystem.reset(PARTICLES_PER_SYSTEM);
                }
                resume();
            }
            // Verifies if position is inside toggle pause button
            if (togglePauseButton.contains(event.getX(), event.getY())) {
                paused = !paused;
            }
        }

        return true;
    }

    // This method is responsible for the continuous update of the
    // drawing view. Every cycle repetition calculates the elapsed
    // elapsed time took to update all drawing, and with this time
    // calculates the actual frames per second (fps) instant rate.
    @Override
    public void run() {
        while (drawing) {
            // Records starting time
            long frameStartTime = System.currentTimeMillis();
            if (!paused) {
                update();
            }
            draw();
            // Calculates the elapsed time
            long timeThisFrame = System.currentTimeMillis() - frameStartTime;
            // Calculates the instant fps
            if (timeThisFrame > 0) {
                fps = MILLIS_IN_SECOND / timeThisFrame;
            }
        }
    }

    // When activity resumes, this view also resumes,
    // starting the thread and letting the live drawing
    // cycle to occurs.
    public void resume() {
        drawing = true;
        thread = new Thread(this);
        thread.start();
    }

    // When activity pauses, this view also pauses.
    // stopping the thread and the live drawing cycle.
    public void pause() {
        drawing = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // </editor-fold>

    // <editor-fold desc="PRIVATE METHODS">

    // This method updates all particle systems (only
    // those who are running).
    private void update() {
        for (ParticleSystem particleSystem : particleSystems) {
            if (particleSystem.getRunning()) {
                particleSystem.update(fps);
            }
        }
    }

    // This method is the main method to draw a frame in
    // any given moment.
    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            // Gets a canvas object
            canvas = surfaceHolder.lockCanvas();
            // Sets background color, text color and size
            canvas.drawColor(Color.argb(255, 0,0,0));
            paint.setColor(Color.argb(255, 255,255,255));
            paint.setTextSize(fontSize);
            if (DEBUGGING) {
                printDebuggingText();
            }
            // Sets color for the buttons and draw them with a letter
            paint.setColor(Color.argb(255, 255,0,255));
            canvas.drawRoundRect(resetButton, 20f, 20f, paint);
            canvas.drawRoundRect(togglePauseButton, 20f, 20f, paint);
            paint.setColor(Color.argb(255, 0,0,0));
            canvas.drawText("R", 35f, 65f, paint);
            if (paused) {
                canvas.drawText("S", 35f, 215f, paint);
            } else {
                canvas.drawText("P", 35f, 215f, paint);
            }
            // Draw all particles systems
            for (ParticleSystem particleSystem : particleSystems) {
                particleSystem.draw(canvas, paint);
            }
            // Updates the view with the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    // This methods prints debugging information into
    // the display.
    private void printDebuggingText() {
        float debugSize = fontSize / 2f;
        float debugStart = 450f;
        paint.setTextSize(debugSize);
        int visibleSystems = 0;
        for (ParticleSystem system : particleSystems) {
            visibleSystems += system.getVisible() ? 1 : 0;
        }
        canvas.drawText("FPS:" + fps,
                10f, debugStart + debugSize, paint);
        canvas.drawText("Systems: " + visibleSystems,
                10f, fontMargin + debugStart + debugSize * 2f, paint);
        canvas.drawText("Particles: " + visibleSystems * PARTICLES_PER_SYSTEM,
                10f, fontMargin + debugStart + debugSize * 3f, paint);
    }

    // </editor-fold>
}

/*
 *  Reference: Horton (2018). Android Programming for Beginners
 */