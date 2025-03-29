package com.example.tempora.composables.alarms.notification


import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.tempora.MainActivity
import com.example.tempora.R
import com.example.tempora.composables.alarms.notification.broadcastreciever.NotificationReceiver
import com.example.tempora.data.models.CurrentWeather

fun showNotification(context: Context, result: CurrentWeather) {
    val channelId = "message_channel"
    val NOTIFICATION_ID = 1001  // Ensure this ID is used consistently

    // Open Notification -> Navigation - Uses Deep Linking
    val replyIntent = Intent(context, MainActivity::class.java).apply {
        action = Intent.ACTION_VIEW
        data = "Home Screen".toUri()
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        putExtra("notification_id", NOTIFICATION_ID) // Pass notification ID to MainActivity
    }

    val replyPendingIntent = PendingIntent.getActivity(
        context, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Cancel Notification -> Dismiss
    val cancelIntent = Intent(context, NotificationReceiver::class.java).apply {
        action = "ACTION_DISMISS_NOTIFICATION"
        putExtra("notification_id", NOTIFICATION_ID)
    }

    val cancelPendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        cancelIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.tempora)
        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.tempora))
        .setContentTitle("${result.name} - ${result.sys.country}")
        .setContentText("Location's Description: ${result.weather[0].description}")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .addAction(R.drawable.tempora, "Open", replyPendingIntent) // Navigate To Home Screen
        .addAction(R.drawable.tempora, "Cancel", cancelPendingIntent) // Dismiss Notification
        .build()

    with(NotificationManagerCompat.from(context)) {
        notify(NOTIFICATION_ID, notification)
    }
}
