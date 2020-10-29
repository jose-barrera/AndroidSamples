package com.jlbc.bouncingfigureskotlin

import android.graphics.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

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
class Triangle(center: PointF) : Figure {

    // <editor-fold desc="INTERNAL FIELDS">

    // Stores the path that defines defines the triangle
    private val path : Path
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
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.isAntiAlias = true
        // Calculates the points of an inscribed equilateral triangle inside a
        // circle of random radius. The algorithm implemented considers
        // the point received as the geometric center of the triangle.
        // NOTE: These calculations are over a standard cartesian plane,
        // where positive Y values are over the origin, but in phone display,
        // the cartesian Y values are below origin (inverted).
        val radius = Random.nextInt(50, 100).toFloat()
        var radians = 30f * Math.PI / 180f
        val vertexA = PointF(
            (cos(radians) * radius).toFloat() + center.x,
            (sin(radians) * radius).toFloat() + center.y)
        radians = 150f * Math.PI / 180f
        val vertexB = PointF(
            (cos(radians) * radius).toFloat() + center.x,
            (sin(radians) * radius).toFloat() + center.y)
        radians = 270f * Math.PI / 180f
        val vertexC = PointF(
            (cos(radians) * radius).toFloat() + center.x,
            (sin(radians) * radius).toFloat() + center.y)
        path = Path()
        path.fillType = Path.FillType.EVEN_ODD
        path.moveTo(vertexA.x, vertexA.y)
        path.lineTo(vertexB.x, vertexB.y)
        path.lineTo(vertexC.x, vertexC.y)
        path.close()
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method updates the position using
    // the velocity vector (increments each vertex
    // with the corresponding x,y components of the velocity.
    override fun update(dimensions: RectF) {
        // A is the top vertex, B is the left one,
        // C is the right one.
        path.offset(velocity.x, velocity.y)
        // Checks if reaches left or right border.
        val bounds = RectF()
        path.computeBounds(bounds, true)
        if (bounds.left < dimensions.left || bounds.right > dimensions.right) {
            velocity.x *= -1f
        }
        // Checks if reaches top or bottom border
        if (bounds.top < dimensions.top || bounds.bottom > dimensions.bottom) {
            velocity.y *= -1f
        }
    }

    // This method draw the triangle, using the graphic
    // objects received.
    override fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    // </editor-fold>
}