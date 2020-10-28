package com.jlbc.livedrawingjava;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.Random;

/*
 *  This class represents a particle system.
 *
 *  PROPERTIES:
 *      running
 *      visible
 *
 *  METHODS
 *      init
 *      reset
 *      emit
 *      update
 *      draw
 */
public class ParticleSystem {

    // <editor-fold desc="INTERNAL FIELDS">

    private Random random = new Random();
    private ArrayList<Particle> particles = new ArrayList<>();
    private float duration = 0f;
    private boolean running = false;
    private boolean visible = false;

    // </editor-fold>

    // <editor-fold desc="PUBLIC PROPERTIES">

    public boolean getRunning() {
        return running;
    }

    public boolean getVisible() {
        return visible;
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    // This method creates the particles of the system.
    public void init(int numParticles) {
        // Sets the condition of the system
        running = false;
        // Clears all particles that may exists
        particles.clear();
        for (int i = 0; i < numParticles; i++) {
            // Creates a random velocity vector (random angle in radians
            // and random magnitude between 1 and 10
            double angle = Math.toRadians(random.nextInt(360));
            double magnitude = random.nextInt(10) + 1;
            PointF velocity = new PointF(
                    (float)(Math.cos(angle) * magnitude),
                    (float)(Math.sin(angle) * magnitude));
            // Creates a particle and adds to the system
            particles.add(new Particle(velocity));
        }
    }

    // This method resets the particle system, basically
    // hides it and decrements the visible systems counter.
    public void reset(int numParticles) {
        if (visible) {
            visible = false;
            init(numParticles);
        }
    }

    // This method initiates the emission of particles
    // establishing all particles at same start position.
    public void emit(PointF startPosition) {
        running = true;
        visible = true;
        // Sets the duration of emission
        duration = 2f;
        // Sets the position of all particles
        for (Particle particle : particles) {
            particle.setPosition(startPosition);
        }
    }

    // This method updates all particles of the system and
    // verifies if system has reach its duration.
    public void update(long fps) {
        // Updates all particles
        for (Particle particle : particles) {
            particle.update(fps);
        }
        // Check if duration is reached
        duration -= (1f / fps);
        if (duration < 0) {
            running = false;
        }
    }

    // This method draws all particles in each frame. The system must
    // be visible.
    public void draw(Canvas canvas, Paint paint) {
        if (visible) {
            for (Particle particle : particles) {
                // Generates a random color to paint
                paint.setARGB(255,
                        random.nextInt(256),
                        random.nextInt(256),
                        random.nextInt(256));
                // Establishes the size of the particles
                float sizeX = 10;
                float sizeY = 10;
                // Draws the particle as a square
                canvas.drawRect(
                        particle.getPosition().x,
                        particle.getPosition().y,
                        particle.getPosition().x + sizeX,
                        particle.getPosition().y + sizeY,
                        paint);
            }
        }
    }

    // </editor-fold>
}

/*
 *  Reference: Horton (2018). Android Programming for Beginners
 */