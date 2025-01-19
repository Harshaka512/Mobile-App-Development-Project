package com.example.notetaskapp

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class TaskWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            // Retrieve task from SharedPreferences
            val sharedPreferences = context.getSharedPreferences("TaskWidget", Context.MODE_PRIVATE)
            val task = sharedPreferences.getString("task", "No Task Added")

            // Update widget layout
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.widget_task_text, task)

            // Refresh widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}