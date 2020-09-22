package com.jlbc.mathabsk

import kotlin.random.Random

/**
 *
 * @author Dr. José Luis Barrera Canto
 *
 **/

class Problem {

    // <editor-fold desc="PUBLIC PROPERTIES">

    val a : Int?
    val b : Int?
    val operator : Char?
    val solution : Int?
    var answer = -1000
    val result : Boolean
        get() {return solution == answer}

    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">

    init {
        a = Random.nextInt(11, 99)
        b = Random.nextInt(11, 99)
        when (Random.nextInt(3)) {
            0 -> {
                operator = '+'
                solution = a + b
            }
            1 -> {
                operator = '-'
                solution = a - b
            }
            else -> {
                operator = '*'
                solution = a * b
            }
        }
    }

    // </editor-fold>

    // <editor-fold desc="PUBLIC METHODS">

    override fun toString(): String {
        return "$a $operator $b = $solution"
    }

    // </editor-fold>
}