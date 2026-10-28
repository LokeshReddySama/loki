package com.earnsenses.app

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 26) getSystemService(NotificationManager::class.java).createNotificationChannel(
            NotificationChannel("offers", "Dasher offer recommendations", NotificationManager.IMPORTANCE_HIGH))
        if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(36,36,36,36) }
        val title = TextView(this).apply { text = "Earn Senses — V1"; textSize = 26f }
        root.addView(title)
        root.addView(TextView(this).apply { text = "Dasher-only notification test. No screen recording, no other app analysis, no automatic acceptance. Target: AUD $35 gross/hour."; textSize = 15f })
        root.addView(Button(this).apply { text = "Enable notification access"; setOnClickListener { startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) } })
        val status = TextView(this).apply { text = "Waiting for Dasher notifications..."; textSize = 16f }
        root.addView(status)
        val offer = EditText(this).apply { hint = "Offer payment (AUD)"; inputType = 8194 }
        val minutes = EditText(this).apply { hint = "Total estimated minutes"; inputType = 8194 }
        val distance = EditText(this).apply { hint = "Total km (optional)"; inputType = 8194 }
        root.addView(offer); root.addView(minutes); root.addView(distance)
        val result = TextView(this).apply { textSize = 20f }
        root.addView(Button(this).apply { text = "Calculate ACCEPT / DECLINE"; setOnClickListener {
            val p = offer.text.toString().toDoubleOrNull(); val m = minutes.text.toString().toDoubleOrNull(); val km = distance.text.toString().toDoubleOrNull()
            result.text = if (p == null || m == null || m <= 0) "Enter valid payment and minutes" else {
                val hourly = p * 60 / m
                val decision = when { hourly >= 35 -> "ACCEPT (gross target met)"; hourly >= 30 -> "CONSIDER"; else -> "DECLINE" }
                "$decision — $%.2f/hour%s. Before fuel, tax and return travel.".format(hourly, if (km != null && km > 0) ", $%.2f/km".format(p/km) else "")
            }
        } })
        root.addView(result)
        root.addView(TextView(this).apply { text = "Privacy: this app filters incoming notifications to the Dasher package only. Android's notification-access permission itself is broader; this V1 does not capture the screen. It cannot reliably extract offers or confirm accept/reject/cancel yet."; textSize = 12f })
        val scroll = ScrollView(this); scroll.addView(root); setContentView(scroll)
        val prefs = getSharedPreferences("dasher", MODE_PRIVATE)
        fun refresh() { status.text = "Last Dasher notification: " + prefs.getString("last", "None detected") }
        refresh()
        root.addView(Button(this).apply { text = "Refresh last Dasher notification"; setOnClickListener { refresh() } })
    }
}
