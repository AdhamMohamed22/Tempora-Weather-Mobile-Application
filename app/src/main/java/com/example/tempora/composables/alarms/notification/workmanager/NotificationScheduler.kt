package com.example.tempora.composables.alarms.notification.workmanager

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun scheduleNotification(context: Context, delayInMillis: Long) {
    val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}
