package com.jlbc.bouncingfigureskotlin

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import kotlin.math.sin
import kotlin.math.cos
import kotlin.random.Random

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
class Circle(private val center: PointF) : Figure {

    // <editor-fold desc="INTERNAL FIELDS">

    // Creates a random radius between 50 and 100
    private val radius = Random.nextInt(50, 100).toFloat()
    private val velocity: PointF
    private val paint: Paint

    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">

    init {
        // Creates a random velocity vector (random angle in radians
        // and random magnitude between 1 and 10
        val angle = Random.nextInt(360) * Math.PI / 180
        val magnitude = Random.nextInt(1, 10).toFloat()
        velocity = PointF(
            (cos(angle) * magnitude).toFloat(),
            (sin(angle) * magnitude).toFloat()
        )
        // Creates a random color
        paint = Paint()
        paint.setARGB(
            Random.nextInt(128) + 128,
            Random.nextInt(128) + 128,
            Random.nextInt(128) + 128,
            Random.nextInt(128) + 128)
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method updates the position using
    // the velocity vector (increments its x,y
    // components with the corresponding x,y
    // components of the velocity.
    override fun update(dimensions: RectF) {
        center.x += velocity.x
        center.y += velocity.y
        // Checks if reaches left or right border
        if (center.x - radius < dimensions.left || center.x + radius > dimensions.right) {
            velocity.x *= -1f
        }
        // Checks if reaches top or bottom border
        if (center.y - radius < dimensions.top || center.y + radius > dimensions.bottom) {
            velocity.y *= -1f
        }
    }

    // This method draw the circle, using the graphic
    // objects received.
    override fun draw(canvas: Canvas) {
        canvas.drawCircle(center.x, center.y, radius, paint)
    }

    // </editor-fold>
}