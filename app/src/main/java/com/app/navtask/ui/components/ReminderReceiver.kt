package com.app.navtask.ui.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class ReminderReceiver: BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onReceive(context: Context?, intent: Intent?) {
        val scheduleNotificationService = context?.let { NotificationHandler(it) }
        val title: String? = intent?.getStringExtra("title")
        scheduleNotificationService?.sendReminderNotification(title)
    }
}