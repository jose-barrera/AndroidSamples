package com.jlbc.livedrawingkotlin

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/*
 *  This class represents a particle system.
 *
 *  PROPERTIES:
 *      running
 *      visible
 *
 *  METHODS
 *      init
 *      emit
 *      update
 *      draw
 */
class ParticleSystem {

    // <editor-fold desc="INTERNAL FIELDS">

    private val particles = ArrayList<Particle>()
    private var duration = 0f

    // </editor-fold>

    // <editor-fold desc="PUBLIC PROPERTIES">

    var running = false
    var visible = false

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method creates the particles of the system.
    fun init(numParticles: Int) {
        // Sets the condition of the system
        running = false
        // Clears all particles that may exists
        particles.clear()
        for (i in 1..numParticles) {
            // Creates a random velocity vector (random angle in radians
            // and random magnitude between 1 and 10
            val angle = Random.nextInt(360) * Math.PI / 180
            val magnitude = Random.nextInt(1, 10)
            val velocity = PointF(
                (cos(angle) * magnitude).toFloat(),
                (sin(angle) * magnitude).toFloat())
            // Creates a particle and adds to the system
            particles.add(Particle(velocity))
        }
    }

    // This method resets the particle system, basically
    // hides it and decrements the visible systems counter.
    fun reset(numParticles: Int) {
        if (visible) {
            visible = false
            init(numParticles)
        }
    }

    // This method initiates the emission of particles
    // establishing all particles at same start position.
    fun emit(startPosition: PointF) {
        running = true
        visible = true
        // Sets the duration of emission
        duration = 2f
        // Sets the position of all particles
        particles.forEach {
            // IMPORTANT: In Java version, the setter "copies" the
            // components x,y of the parameters. Here in Kotlin, to
            // avoid use the same object reference, we need to create
            // a new PointF object for each particle.
            it.position = PointF(startPosition.x, startPosition.y)
        }
    }

    // This method updates all particles of the system and
    // verifies if system has reach its duration.
    fun update(fps: Long) {
        // Updates all particles
        particles.forEach {
            it.update(fps)
        }
        // Check if duration is reached
        duration -= (1f / fps)
        if (duration < 0) {
            running = false
        }
    }

    // This method draws all particles in each frame. The system must
    // be visible.
    fun draw(canvas: Canvas, paint: Paint) {
        if (visible) {
            particles.forEach {
                // Generates a random color to paint
                paint.setARGB(255,
                    Random.nextInt(256),
                    Random.nextInt(256),
                    Random.nextInt(256))
                // Establishes the size of the particles
                val sizeX = 10f
                val sizeY = 10f
                // Draws the particle as a square
                canvas.drawRect(
                    it.position.x,
                    it.position.y,
                    it.position.x + sizeX,
                    it.position.y + sizeY,
                    paint)
            }
        }
    }

    // </editor-fold>
}

/*
 *  Reference: Horton (2018). Android Programming for Beginners
 */