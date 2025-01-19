package com.example.notetaskapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.notetaskapp.databinding.ActivityTimerBinding
import java.util.Locale

class TimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding
    private var seconds = 0
    private var running = false
    private var wasRunning = false
    private val handler = Handler(Looper.getMainLooper())

    private val updateRunnable = object : Runnable {
        override fun run() {
            if (running) {
                seconds++
                updateTimer()
            }
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        }

        binding.buttonStart.setOnClickListener {
            running = true
        }

        binding.buttonPause.setOnClickListener {
            running = false
        }

        binding.buttonReset.setOnClickListener {
            running = false
            seconds = 0
            updateTimer()
        }

        handler.post(updateRunnable)
    }

    private fun updateTimer() {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        binding.textViewTimer.text = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("seconds", seconds)
        outState.putBoolean("running", running)
        outState.putBoolean("wasRunning", wasRunning)
    }

    override fun onResume() {
        super.onResume()
        if (wasRunning) {
            running = true
        }
    }

    override fun onPause() {
        super.onPause()
        wasRunning = running
        running=false
        }
}