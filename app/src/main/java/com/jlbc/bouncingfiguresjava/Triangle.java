package com.jlbc.bouncingfiguresjava;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Random;

/*
 *  This class represents an equilateral triangle that moves and bounces on the screen.
 *
 *  PROPERTIES:
 *      n/a
 *
 *  METHODS
 *      update
 *      draw
 */
public class Triangle implements Figure {

    // <editor-fold desc="INTERNAL FIELDS">

    // Stores the path that defines defines the triangle
    private Path path;
    private PointF velocity;
    private Paint paint;

    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">

    public Triangle(PointF center) {

        // Creates a random velocity vector (random angle in radians
        // and random magnitude between 1 and 10
        Random random = new Random();
        float angle = (float)Math.toRadians(random.nextInt(360));
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
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        // Calculates the points of an inscribed equilateral triangle inside a
        // circle of random radius. The algorithm implemented considers
        // the point received as the geometric center of the triangle.
        // NOTE: These calculations are over a standard cartesian plane,
        // where positive Y values are over the origin, but in phone display,
        // the cartesian Y values are below origin (inverted).
        float radius = random.nextInt(51) + 50;
        float radians = (float)Math.toRadians(30);
        PointF vertexA = new PointF(
                (float)(Math.cos(radians) * radius) + center.x,
                (float)(Math.sin(radians) * radius) + center.y);
        radians = (float)Math.toRadians(150);
        PointF vertexB = new PointF(
                (float)(Math.cos(radians) * radius) + center.x,
                (float)(Math.sin(radians) * radius) + center.y);
        radians = (float)Math.toRadians(270);
        PointF vertexC = new PointF(
                (float)(Math.cos(radians) * radius) + center.x,
                (float)(Math.sin(radians) * radius) + center.y);
        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(vertexA.x, vertexA.y);
        path.lineTo(vertexB.x, vertexB.y);
        path.lineTo(vertexC.x, vertexC.y);
        path.close();
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method updates the position using
    // the velocity vector (increments each vertex
    // with the corresponding x,y components of the velocity.
    @Override
    public void update(RectF dimensions) {
        // A is the top vertex, B is the left one,
        // C is the right one.
        path.offset(velocity.x, velocity.y);
        // Checks if reaches left or right border.
        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        if (bounds.left < dimensions.left || bounds.right > dimensions.right) {
            velocity.x *= -1f;
        }
        // Checks if reaches top or bottom border
        if (bounds.top < dimensions.top || bounds.bottom > dimensions.bottom) {
            velocity.y *= -1f;
        }
    }

    // This method draw the triangle, using the graphic
    // objects received.
    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    // </editor-fold>
}
