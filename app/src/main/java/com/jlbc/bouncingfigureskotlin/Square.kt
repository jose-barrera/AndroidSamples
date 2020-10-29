package com.jlbc.bouncingfigureskotlin

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

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
class Square(center: PointF) : Figure  {

    // <editor-fold desc="INTERNAL FIELDS">

    // Stores the coordinates of left, top,
    // right, and bottom sides
    private var left: Float
    private var top: Float
    private var right: Float
    private var bottom: Float
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
            (sin(angle) * magnitude).toFloat())
        // Creates a random color
        paint = Paint()
        paint.setARGB(
            Random.nextInt(128) + 128,
            Random.nextInt(128) + 128,
            Random.nextInt(128) + 128,
            Random.nextInt(128) + 128)
        // Calculates the coordinates of square. The algorithm implemented
        // considers the point received as the geometric center of the square,
        // so it is necessary to calculate left-right coordinates (along X-axis)
        // and top-bottom coordinates (along Y-axis). These coordinates are
        // calculated adding or subtracting half the size to the center coordinates.
        // Creates a random size between 50 and 100
        val size = Random.nextInt(50, 100).toFloat()
        left = center.x - size / 2f
        top = center.y - size / 2f
        right = center.x + size / 2f
        bottom = center.y + size / 2f
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method updates the position using
    // the velocity vector (increments each coordinate
    // with the corresponding x,y components of the velocity.
    override fun update(dimensions: RectF) {
        // Coordinates along X-axis
        left += velocity.x
        right += velocity.x
        // Coordinates along Y-axis
        top += velocity.y
        bottom += velocity.y
        // Checks if reaches left or right border
        if (left < dimensions.left || right > dimensions.right) {
            velocity.x *= -1f
        }
        // Checks if reaches top or bottom border
        if (top < dimensions.top || bottom > dimensions.bottom) {
            velocity.y *= -1f
        }
    }

    // This method draw the circle, using the graphic
    // objects received.
    override fun draw(canvas: Canvas) {
        canvas.drawRect(left, top, right, bottom, paint)
    }

    // </editor-fold>
}