package com.example.tempora.composables.alarms.notification.broadcastreciever

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    @SuppressLint("ServiceCast")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "ACTION_DISMISS_NOTIFICATION") {
            val notificationId = intent.getIntExtra("notification_id", 0)
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId) // This removes the notification
        }
    }
}
