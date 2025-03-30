package com.example.tempora.composables.alarms.notification.sound

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.tempora.MainActivity
import com.example.tempora.composables.alarms.notification.stopNotificationSound

class StopSoundReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Stop the sound
        stopNotificationSound()

        // Remove the notification
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(1001) // Matches NOTIFICATION_ID in showNotification()
    }
}
