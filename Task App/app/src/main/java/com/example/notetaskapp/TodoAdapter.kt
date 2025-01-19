package com.example.notetaskapp

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoAdapter(
    private var items: MutableList<Todo>,
    private val sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item, parent, false)
        return ToDoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.cbTodo.text = items[position].item

        holder.ivDelete.setOnClickListener {
            if (holder.cbTodo.isChecked) {
                items.removeAt(position)
                saveToSharedPreferences() // Save the updated list to SharedPreferences
                notifyItemRemoved(position)
                Toast.makeText(holder.itemView.context, "Item Deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(holder.itemView.context, "Select the Item to Delete", Toast.LENGTH_SHORT).show()
            }
        }

        holder.ivEdit.setOnClickListener {
            val context = holder.itemView.context
            val builder = android.app.AlertDialog.Builder(context)
            val input = EditText(context)
            input.setText(items[position].item)

            builder.setView(input)
            builder.setTitle("Edit Note")

            builder.setPositiveButton("Update") { dialog, _ ->
                val updatedText = input.text.toString()
                if (updatedText.isNotBlank()) {
                    items[position].item = updatedText // Assuming 'item' is mutable
                    saveToSharedPreferences() // Save updated item
                    notifyItemChanged(position)
                    Toast.makeText(context, "Item Updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Item cannot be empty", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            builder.show()
        }
    }

    // Insert a new item
    fun addItem(newItem: Todo) {
        items.add(newItem)
        saveToSharedPreferences() // Save updated list
        notifyItemInserted(items.size - 1)
    }

    // Save updated task list to SharedPreferences
    private fun saveToSharedPreferences() {
        CoroutineScope(Dispatchers.IO).launch {
            val json = Gson().toJson(items)  // Convert list to JSON
            withContext(Dispatchers.Main) {
                sharedPreferences.edit().putString("tasks", json).apply()
            }
        }
    }

    // Method to get the current list of todo items
    fun getTodoList(): List<Todo> {
        return items
    }
}