package com.example.notetaskapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notetaskapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var todoAdapter: TodoAdapter
    private val gson = Gson()

    private lateinit var countdownText: TextView
    private var secondsElapsed: Int = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)

        val rvTodoList: RecyclerView = findViewById(R.id.rvTodoList)
        rvTodoList.layoutManager = LinearLayoutManager(this)

        // Load data from SharedPreferences
        val todoList = getTodoItemsFromSharedPreferences()
        todoAdapter = TodoAdapter(todoList.toMutableList(), sharedPreferences)
        rvTodoList.adapter = todoAdapter

        val btnAddItem: Button = findViewById(R.id.btnAddItem)
        btnAddItem.setOnClickListener {
            displayDialog()
        }

        // Countdown TextView to display time spent on adding events
        countdownText = findViewById(R.id.countdownText)

        // Button to navigate to TimerActivity
        val buttonTimer: Button = findViewById(R.id.buttonTimer)
        buttonTimer.setOnClickListener {
            navigateToTimerActivity() // Navigate to TimerActivity
        }

        // Button to navigate to WidgetActivity
        val button3: Button = findViewById(R.id.button3)
        button3.setOnClickListener {
            val intent = Intent(this, WidgetActivity::class.java)
            startActivity(intent)
        }

        // Start counting up when the activity is created
        startCountdown()
    }

    // Method to start the countdown timer
    private fun startCountdown() {
        handler.post(object : Runnable {
            override fun run() {
                countdownText.text = "Time Spent On Add Event(s): $secondsElapsed seconds"
                secondsElapsed++
                handler.postDelayed(this, 1000) // Update every second
            }
        })
    }

    override fun onPause() {
        super.onPause()
        // Stop the countdown when the app goes to the background
        handler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        // Reset the countdown when the app is resumed
        secondsElapsed = 0
        startCountdown()
    }

    // Get list of Todo items from SharedPreferences
    private fun getTodoItemsFromSharedPreferences(): List<Todo> {
        val json = sharedPreferences.getString("tasks", null)
        return if (json != null) {
            val type = object : TypeToken<List<Todo>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }

    // Dialog to add a new Todo item
    private fun displayDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Task")
        builder.setMessage("Insert Task to TaskMemo")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Ok") { dialog, _ ->
            val item = input.text.toString()
            if (item.isNotBlank()) {
                val newTodo = Todo(item)
                todoAdapter.addItem(newTodo) // Add new item to the adapter
                // The list is automatically saved in the adapter
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    // Function to navigate to TimerActivity
    private fun navigateToTimerActivity() {
        val intent = Intent(this, TimerActivity::class.java)
        startActivity(intent)
    }
}
