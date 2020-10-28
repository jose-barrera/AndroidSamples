package com.jlbc.livedrawingjava;

import android.graphics.PointF;

/*
 *  This class represents a specific particle.
 *
 *  PROPERTIES:
 *      position
 *
 *  METHODS
 *      update
 */
public class Particle {

    // <editor-fold desc="INTERNAL FIELDS">

    private PointF position;
    private PointF velocity;

    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">

    public Particle(PointF velocity) {
        this.position = new PointF();
        this.velocity = new PointF();

        // Copies the value of the parameter
        // to the internal field
        this.velocity.x = velocity.x;
        this.velocity.y = velocity.y;
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC PROPERTIES">

    public PointF getPosition() {
        return position;
    }
    public void setPosition(PointF position) {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method updates the position using
    // the velocity vector (increments its x,y
    // components with the corresponding x,y
    // components of the velocity.
    public void update(long fps) {
        position.x += velocity.x / fps;
        position.y += velocity.y / fps;
    }

    // </editor-fold>
}

/*
 *  Reference: Horton (2018). Android Programming for Beginners
 */