package com.jlbc.bouncingfigureskotlin

import android.graphics.Canvas
import android.graphics.RectF

/*
 *  This interface represents the behaviour of any figure.
 *
 *  METHODS
 *      update
 *      draw
 */
interface Figure {

    fun update(dimensions: RectF)
    fun draw(canvas: Canvas)

}