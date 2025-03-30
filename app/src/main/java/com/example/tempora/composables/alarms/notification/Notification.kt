package com.example.tempora.composables.alarms.notification


import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.tempora.MainActivity
import com.example.tempora.R
import com.example.tempora.composables.alarms.notification.broadcastreciever.NotificationReceiver
import com.example.tempora.composables.alarms.notification.sound.StopSoundReceiver
import com.example.tempora.data.models.CurrentWeather

fun showNotification(context: Context, result: CurrentWeather) {
    val channelId = "message_channel"
    val NOTIFICATION_ID = 1001

    // Intent to stop sound and remove notification
    val stopSoundIntent = Intent(context, StopSoundReceiver::class.java)
    val stopSoundPendingIntent = PendingIntent.getBroadcast(
        context, 0, stopSoundIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Intent to stop sound, open MainActivity, and remove notification
    val openIntent = Intent(context, StopSoundReceiver::class.java).apply {
        putExtra("open_main", true)
    }
    val openPendingIntent = PendingIntent.getBroadcast(
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
        .addAction(R.drawable.tempora, "Cancel", stopSoundPendingIntent) // Stop Sound & Dismiss
        .setAutoCancel(true) // Auto remove when clicked
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
