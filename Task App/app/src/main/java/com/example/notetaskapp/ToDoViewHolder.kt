package com.example.notetaskapp

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.notetaskapp.R

class ToDoViewHolder(view: View):RecyclerView.ViewHolder(view) {

    val ivEdit :ImageView
    val cbTodo:CheckBox
    val ivDelete:ImageView

    init {
        cbTodo = view.findViewById(R.id.cbTodo)
        ivDelete = view.findViewById(R.id.ivDelete)
        ivEdit=view.findViewById(R.id.ivEdit)

        }
}