package com.ssas.jibli.agent.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.data.prefs.PrefKeys
import com.ssas.jibli.agent.ui.auth.LoginActivity
import com.ssas.jibli.agent.ui.home.DashboardActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("jaspal", "<<<<<<<<<<<<< Fire Token>>>>>>>> $token")
        var pref= getSharedPreferences(PrefKeys.PREF_NAME, Context.MODE_PRIVATE)
        pref?.edit()?.putString(PrefKeys.FIREBASE_TOKEN, token)?.apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("jaspal", "Jibli Message data payload: ${remoteMessage.data}")
            var title = remoteMessage.data["title"]
            var message = remoteMessage.data["text"]
            var type = remoteMessage.data["type"]
            var image = remoteMessage.data["image"]
            // {text=Hello Jaspal,  First Test notification, type=message, image=PushNotificationLogo.png, title=DP FCM Notificatoin Title}
            sendNotification(title, message, type, image)
        }
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            var title = it.title
            var message = it.body
            Log.d("jaspal", "Message Notification Body: ${it.body}")
            Log.d("jaspal", "Message Notification Body: ${it.title}")
            sendNotification(title, message, "", "")
        }
    }

    private fun sendNotification(
        messageTitle: String?,
        messageBody: String?,
        type: String?,
        image: String?
    ) {
        var pref= getSharedPreferences(PrefKeys.PREF_NAME, Context.MODE_PRIVATE)
        val num = System.currentTimeMillis().toInt()
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent: Intent? = Intent(this, DashboardActivity::class.java)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent?.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                num /* Request code */,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        var loginIntent = Intent(this, LoginActivity::class.java)
        val loginPendingIntent =
            PendingIntent.getActivity(
                this,
                num /* Request code */,
	            loginIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val CHANNEL_ID = getString(R.string.channel_id)
            val name = getString(R.string.channel_name)
            val Description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = Description
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mChannel.setShowBadge(false)
            notificationManager.createNotificationChannel(mChannel)
            val builder = Notification.Builder(this, CHANNEL_ID)
            builder.setSmallIcon(R.drawable.ic_notification)
            builder.setColor(ActivityCompat.getColor(applicationContext, R.color.colorPrimary))
            //builder.setCustomContentView(contentViewSmall)
            //builder.setCustomBigContentView(contentViewSmall)
            builder.setAutoCancel(true)
            builder.setContentTitle(messageTitle)
            builder.style = Notification.BigTextStyle().bigText(messageBody)
            builder.setContentText(messageBody)
            if (pref?.getString(PrefKeys.SALES_AGENT_CODE,"").isNullOrEmpty()) {
                builder.setContentIntent(loginPendingIntent)
            }else{
                builder.setContentIntent(pendingIntent)
            }
            notificationManager.notify(num, builder.build())
        } else {
            val builder = NotificationCompat.Builder(this)
            builder.setSmallIcon(R.drawable.ic_notification)
            builder.color = ActivityCompat.getColor(applicationContext, R.color.colorPrimary)
            builder.setAutoCancel(true)
            builder.setContentTitle(messageTitle)
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
            builder.setContentText(messageBody)
            //builder.setCustomContentView(contentViewSmall)
            //builder.setCustomBigContentView(contentViewSmall)
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            if (pref?.getString(PrefKeys.SALES_AGENT_CODE,"").isNullOrEmpty()) {
                builder.setContentIntent(loginPendingIntent)
            }else{
                builder.setContentIntent(pendingIntent)
            }
            notificationManager.notify(num, builder.build())
        }
    }

    fun cancelNotification(manager: NotificationManager, notifyId: Int) {
        manager.cancel(notifyId)
    }

}