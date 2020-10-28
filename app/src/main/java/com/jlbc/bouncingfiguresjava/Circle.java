package com.jlbc.bouncingfiguresjava;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import java.util.Random;

/*
 *  This class represents a circles that moves and bounces on the screen.
 *
 *  PROPERTIES:
 *      n/a
 *
 *  METHODS
 *      update
 *      draw
 */
public class Circle implements Figure {

    // <editor-fold desc="INTERNAL FIELDS">

    private float radius;
    private PointF center;
    private PointF velocity;
    private Paint paint;

    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">

    public Circle(PointF center) {
        Random random = new Random();
        // Copies the value of the parameter
        this.center = new PointF(center.x, center.y);
        // Creates a random radius between 50 and 100
        this.radius = random.nextInt(51) + 50;
        // Creates a random velocity vector (random angle in radians
        // and random magnitude between 1 and 10
        double angle = Math.toRadians(random.nextInt(360));
        double magnitude = random.nextInt(10) + 1;
        this.velocity = new PointF(
                (float)(Math.cos(angle) * magnitude),
                (float)(Math.sin(angle) * magnitude));
        // Creates a random color
        paint = new Paint();
        paint.setARGB(
                random.nextInt(128) + 128,
                random.nextInt(128) + 128,
                random.nextInt(128) + 128,
                random.nextInt(128) + 128);
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method updates the position using
    // the velocity vector (increments its x,y
    // components with the corresponding x,y
    // components of the velocity.
    @Override
    public void update(RectF dimensions) {
        center.x += velocity.x;
        center.y += velocity.y;
        // Checks if reaches left or right border
        if ((center.x - radius) < dimensions.left || (center.x + radius) > dimensions.right) {
            velocity.x *= -1;
        }
        // Checks if reaches top or bottom border
        if ((center.y - radius) < dimensions.top || (center.y + radius) > dimensions.bottom) {
            velocity.y *= -1;
        }
    }

    // This method draw the circle, using the graphic
    // objects received.
    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(center.x, center.y, radius, paint);
    }

    // </editor-fold>
}
