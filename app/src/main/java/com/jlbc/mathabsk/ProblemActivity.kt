package com.jlbc.mathabsk

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProblemActivity : AppCompatActivity() {

    private val problems = arrayOfNulls<Problem>(10)
    private val times = LongArray(10)
    private var problem: Problem? = null
    private var viewProblem: TextView? = null
    private var editAnswer: EditText? = null
    private var imm: InputMethodManager? = null
    private var start: Long = 0
    private var i = 0
    private var correct = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        for (i in 0..9) {
            problems[i] = Problem()
            Log.i("info", problems[i].toString())
        }
        setContentView(R.layout.activity_problem)

        viewProblem = findViewById<View>(R.id.textProblem) as TextView
        editAnswer = findViewById<View>(R.id.editAnswer) as EditText
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        editAnswer!!.setOnEditorActionListener(OnEditorActionListener { _, actionID, _ ->
            when (actionID) {
                EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_NEXT, EditorInfo.IME_ACTION_PREVIOUS -> {
                    times[i] = System.currentTimeMillis() - start
                    problem!!.answer = editAnswer!!.text.toString().toInt()
                    correct += if (problem!!.result) 1 else 0
                    if (i < 9) {
                        i++
                        displayProblem()
                        imm!!.showSoftInput(editAnswer, InputMethodManager.SHOW_FORCED)
                    } else {
                        Toast.makeText(
                            this@ProblemActivity,
                            "Correct: $correct",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@OnEditorActionListener true
                }
            }
            true
        })
    }

    override fun onResume() {
        super.onResume()
        displayProblem()
        imm!!.showSoftInput(editAnswer, InputMethodManager.SHOW_FORCED)
    }

    private fun displayProblem() {
        problem = problems[i]
        viewProblem!!.text = "${problem!!.a} ${problem!!.operator} ${problem!!.b}"
        editAnswer!!.setText("")
        editAnswer!!.requestFocus()
        start = System.currentTimeMillis()
    }

}