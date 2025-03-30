package com.example.tempora.composables.alarms.notification.sound

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.tempora.MainActivity
import com.example.tempora.composables.alarms.notification.stopNotificationSound

class StopSoundReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        stopNotificationSound()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(1001) // Ensure the correct ID is used

        if (intent.getBooleanExtra("open_main", false)) {
            val mainIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(mainIntent)
        }
    }
}
