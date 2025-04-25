package com.muhasib.documate

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.FirebaseApp

class DocumateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
        FirebaseApp.initializeApp(this)
    }
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Item Deletion Channel"
            val descriptionText = "Shows notifications when an item is deleted"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("item_deletion_channel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}