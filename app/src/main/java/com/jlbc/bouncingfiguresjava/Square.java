package com.jlbc.bouncingfiguresjava;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Random;

/*
 *  This class represents a square that moves and bounces on the screen.
 *
 *  PROPERTIES:
 *      n/a
 *
 *  METHODS
 *      update
 *      draw
 */
public class Square implements Figure {

    // <editor-fold desc="INTERNAL FIELDS">

    // Stores the coordinates of left, top,
    // right, and bottom sides
    private float left;
    private float top;
    private float right;
    private float  bottom;
    private PointF velocity;
    private Paint paint;

    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">

    public Square(PointF center) {
        // Creates a random velocity vector (random angle in radians
        // and random magnitude between 1 and 10
        Random random = new Random();
        float angle = (float) Math.toRadians(random.nextInt(360));
        float magnitude = random.nextInt(10) + 1;
        velocity = new PointF(
                (float)(Math.cos(angle) * magnitude),
                (float)(Math.sin(angle) * magnitude));
        // Creates a random color
        paint = new Paint();
        paint.setARGB(
                random.nextInt(128) + 128,
                random.nextInt(128) + 128,
                random.nextInt(128) + 128,
                random.nextInt(128) + 128);
        // Calculates the coordinates of square. The algorithm implemented
        // considers the point received as the geometric center of the square,
        // so it is necessary to calculate left-right coordinates (along X-axis)
        // and top-bottom coordinates (along Y-axis). These coordinates are
        // calculated adding or subtracting half the size to the center coordinates.
        // Creates a random size between 50 and 100
        float size = random.nextInt(51) + 50;
        left = center.x - size / 2f;
        top = center.y - size / 2f;
        right = center.x + size / 2f;
        bottom = center.y + size / 2f;
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method updates the position using
    // the velocity vector (increments each coordinate
    // with the corresponding x,y components of the velocity.
    @Override
    public void update(RectF dimensions) {
        // Coordinates along X-axis
        left += velocity.x;
        right += velocity.x;
        // Coordinates along Y-axis
        top += velocity.y;
        bottom += velocity.y;
        // Checks if reaches left or right border
        if (left < dimensions.left || right > dimensions.right) {
            velocity.x *= -1f;
        }
        // Checks if reaches top or bottom border
        if (top < dimensions.top || bottom > dimensions.bottom) {
            velocity.y *= -1f;
        }
    }

    // This method draw the circle, using the graphic
    // objects received.
    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(left, top, right, bottom, paint);
    }

    // </editor-fold>
}
