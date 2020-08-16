package com.jlbc.welcome

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView

class MainActivity : AppCompatActivity()
{
    private var i = 0
    private lateinit var view: TextView
    private val colors = arrayOf(Color.BLUE, Color.WHITE, Color.YELLOW, Color.GREEN)
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view = findViewById<TextView>(R.id.welcome)

        runnable = Runnable {
            i %= colors.size
            view.setTextColor(colors[i])
            i++
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}