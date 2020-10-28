package com.jlbc.bouncingfiguresjava;

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
public class Board extends SurfaceView implements Runnable {

    // <editor-fold desc="INTERNAL FIELDS">

    // Internal fields
    private RectF dimensions;
    // Objects for graphics methods
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint paint;
    // Variables related to threads
    private Thread thread = null;
    private volatile boolean drawing;
    // Collection for figures
    private ArrayList<Figure> figures = new ArrayList<>();

    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">

    public Board(Context context) {
        super(context);
        // Gets drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC PROPERTIES">

    public RectF getDimensions() {
        return dimensions;
    }
    public void setDimensions(float left, float top, float right, float bottom) {
        dimensions = new RectF(left, top, right, bottom);
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method is responsible for processing when the user
    // touches the screen.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            // Creates a circle
            figures.add(new Circle(new PointF(event.getX(), event.getY())));
        }
        return true;
    }

    // This method is responsible for the continuous update of the
    // drawing board. Every cycle repetition updates and draws all
    // figures.
    @Override
    public void run() {
        while (drawing) {
            update();
            draw();
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

    // When activity pauses, this view also pauses,
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

    // This method updates all figures.
    private void update() {
        // NOTE: This "copy" of the ArrayList is needed to avoid
        // ConcurrentModification error. This error can happen if
        // this cycle is executing and user tries to add another figure.
        for (Figure figure : new ArrayList<>(figures)) {
            figure.update(dimensions);
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
            // Draw all figures
            // NOTE: This "copy" of the ArrayList is needed to avoid
            // ConcurrentModification error. This error can happen if
            // this cycle is executing and user tries to add another figure.
            for (Figure figure : new ArrayList<>(figures)) {
                figure.draw(canvas);
            }
            // Updates the view with the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    // </editor-fold>
}
