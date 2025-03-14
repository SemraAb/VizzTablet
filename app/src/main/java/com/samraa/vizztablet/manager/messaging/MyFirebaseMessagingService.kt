package com.samraa.vizztablet.manager.messaging

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e(TAG, "onMessageReceived:  ${remoteMessage.data}", )
        if (remoteMessage.data.isNotEmpty()) {
            val data = remoteMessage.data
            val notificationType = data["NOTIFICATION_TYPE"]

            if (notificationType == "ORDER_LIST_REFRESH") {
                handleOrderListRefresh()
            }
        }
    }

    private fun handleOrderListRefresh() {
        Log.d(TAG, "Received ORDER_LIST_REFRESH for tablet app")
        // Notify the app to refresh the order list by sending a broadcast
        val intent = Intent(ORDER_LIST_REFRESH_ACTION)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        private const val TAG = "TabletFirebaseMessaging"
        const val ORDER_LIST_REFRESH_ACTION = "com.samraa.vizztablet.ORDER_LIST_REFRESH"
    }
}
