package com.example.tempora.composables.alarms.notification

//class NotificationService : Service() {
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val selectedTimeInMillis = intent?.getLongExtra("selectedTime", 0L) ?: 0L
//
//        if (selectedTimeInMillis > 0L) {
//            val now = System.currentTimeMillis()
//            val delay = selectedTimeInMillis - now
//
//            if (delay > 0) {
//                scheduleNotification(this, delay) // Schedule notification with the exact delay
//            } else {
//                Log.e("NotificationService", "Selected time is in the past!")
//            }
//        }
//
//        return START_STICKY
//    }
//
//    override fun onBind(intent: Intent?): IBinder? = null
//}


