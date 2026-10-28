
package com.earnsenses.app

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DasherNotificationService : NotificationListenerService() {

    private val dasherPackages = setOf("com.doordash.driverapp")

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null || sbn.packageName !in dasherPackages) return

        val extras = sbn.notification.extras

        val title = extras.getCharSequence(
            Notification.EXTRA_TITLE
        )?.toString().orEmpty()

        val body = extras.getCharSequence(
            Notification.EXTRA_TEXT
        )?.toString().orEmpty()

        val time = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        getSharedPreferences("dasher", MODE_PRIVATE)
            .edit()
            .putString("last", "$time | $title | $body")
            .apply()
    }
}
