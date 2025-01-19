package com.example.notetaskapp

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WidgetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ensure the correct layout file is referenced here
        setContentView(R.layout.activity_widget)

        val taskInput = findViewById<EditText>(R.id.task_input)
        val saveButton = findViewById<Button>(R.id.save_button)

        saveButton.setOnClickListener {
            val task = taskInput.text.toString()

            if (task.isNotEmpty()) {
                // Save task to SharedPreferences
                val sharedPreferences = getSharedPreferences("TaskWidget", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("task", task)
                editor.apply()

                // Update the widget
                val intent = Intent(this, TaskWidgetProvider::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
                    ComponentName(application, TaskWidgetProvider::class.java)
                )
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                sendBroadcast(intent)

                Toast.makeText(this, "Task saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a task.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
