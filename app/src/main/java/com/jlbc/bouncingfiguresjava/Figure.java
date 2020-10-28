package com.jlbc.bouncingfiguresjava;

import android.graphics.Canvas;
import android.graphics.RectF;

/*
 *  This interface represents the behaviour of any figure.
 *
 *  METHODS
 *      update
 *      draw
 */
public interface Figure {

    void update(RectF dimensions);
    void draw(Canvas canvas);

}
