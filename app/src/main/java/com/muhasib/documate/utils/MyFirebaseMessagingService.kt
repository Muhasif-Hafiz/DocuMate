package com.muhasib.documate.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.muhasib.documate.R

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { notification ->
            showNotification(
                notification.title ?: "Notification",
                notification.body ?: "No message"
            )
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationId = System.currentTimeMillis().toInt()

        val notification = NotificationCompat.Builder(this, "item_deletion_channel")
            .setSmallIcon(R.drawable.ic_google)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        NotificationManagerCompat.from(this).notify(notificationId, notification)
    }

    override fun onNewToken(token: String) {
        // Handle token refresh if needed
    }
}