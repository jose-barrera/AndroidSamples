package com.jlbc.livedrawingkotlin

import android.graphics.PointF

/*
 *  This class represents a specific particle.
 *
 *  PROPERTIES:
 *      position
 *
 *  METHODS
 *      update
 */
class Particle(private val velocity: PointF) {

    // <editor-fold desc="PUBLIC PROPERTIES">

    var position = PointF()

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method updates the position using
    // the velocity vector (increments its x,y
    // components with the corresponding x,y
    // components of the velocity.
    fun update(fps: Long) {
        position.x += velocity.x / fps
        position.y += velocity.y / fps
    }

    // </editor-fold>
}

/*
 *  Reference: Horton (2018). Android Programming for Beginners
 */