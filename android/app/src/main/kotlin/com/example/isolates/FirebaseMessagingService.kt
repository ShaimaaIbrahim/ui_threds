package com.example.isolates

import android.Manifest
import android.content.pm.PackageManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onCreate() {
        super.onCreate()
        Log.d("MyFirebaseMessaging>>>", "onCreate")
    }

    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("MyFirebaseMessaging>>>", "remoteMessage")
        remoteMessage.notification?.let {
            val messageBody = it.body
            val messageTitle = it.title
            val builder = NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Show the notification
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            NotificationManagerCompat.from(this).notify(1, builder.build())
        }
//        // Handle the message
//        Log.d("FCM", "Message received from: ${remoteMessage.from}")
//
//        // You can access the message data and notification here
//        remoteMessage.data.isNotEmpty().let {
//            Log.d("FCM", "Message data payload: ${remoteMessage.data}")
//        }
//
//        remoteMessage.notification?.let {
//            Log.d("FCM", "Message Notification Body: ${it.body}")
//        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MyFirebaseMessaging>>>", "onNewToken")
    }
}