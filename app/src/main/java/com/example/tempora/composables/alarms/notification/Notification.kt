package com.example.tempora.composables.alarms.notification


import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tempora.MainActivity
import com.example.tempora.R
import com.example.tempora.composables.alarms.notification.sound.StopSoundReceiver
import com.example.tempora.data.models.CurrentWeather

fun showNotification(context: Context, result: CurrentWeather) {
    val channelId = "message_channel"
    val NOTIFICATION_ID = 1001

    // Intent to stop sound and remove notification
    val stopSoundIntent = Intent(context, StopSoundReceiver::class.java)
    val stopSoundPendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        stopSoundIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Intent to open MainActivity (with stop sound logic)
    val openIntent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        putExtra("notification_id", NOTIFICATION_ID) // Pass notification ID
    }

    val openPendingIntent = PendingIntent.getActivity(
        context, 1, openIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    playNotificationSound(context) // Start alarm sound

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.tempora)
        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.tempora))
        .setContentTitle("${result.name} - ${result.sys.country}")
        .setContentText("Location's Description: ${result.weather[0].description}")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .addAction(R.drawable.tempora, "Open", openPendingIntent) // Open MainActivity
        .addAction(
            R.drawable.tempora,
            "Cancel",
            stopSoundPendingIntent
        ) // Stop Sound & Remove Notification
        .setAutoCancel(true) // Remove notification when clicked
        .build()

    with(NotificationManagerCompat.from(context)) {
        notify(NOTIFICATION_ID, notification)
    }
}

fun playNotificationSound(context: Context) {
    stopNotificationSound() // Ensure any existing sound stops before playing a new one

    mediaPlayer = MediaPlayer.create(context, R.raw.notification).apply {
        isLooping = true
        start()
    }
}

fun stopNotificationSound() {
    mediaPlayer?.stop()
    mediaPlayer?.release()
    mediaPlayer = null
}
