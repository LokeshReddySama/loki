package com.earnsenses.app

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                "offers",
                "Dasher recommendations",
                NotificationManager.IMPORTANCE_HIGH
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1
            )
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(36, 36, 36, 36)
        }

        layout.addView(TextView(this).apply {
            text = "Earn Senses V1"
            textSize = 26f
        })

        layout.addView(TextView(this).apply {
            text = "DoorDash notification test\nTarget: AUD 35/hour"
            textSize = 16f
        })

        layout.addView(Button(this).apply {
            text = "Enable Notification Access"
            setOnClickListener {
                startActivity(
                    Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                )
            }
        })

        val status = TextView(this).apply {
            textSize = 16f
            text = "Waiting for Dasher notifications"
        }
        layout.addView(status)

        val payment = EditText(this).apply {
            hint = "Offer payment (AUD)"
            inputType = 8194
        }
        val minutes = EditText(this).apply {
            hint = "Estimated delivery minutes"
            inputType = 8194
        }
        layout.addView(payment)
        layout.addView(minutes)

        val result = TextView(this).apply {
            textSize = 20f
        }

        layout.addView(Button(this).apply {
            text = "Calculate"
            setOnClickListener {
                val p = payment.text.toString().toDoubleOrNull()
                val m = minutes.text.toString().toDoubleOrNull()

                result.text = if (p == null || m == null || m <= 0) {
                    "Enter valid payment and time"
                } else {
                    val hourly = p * 60 / m
                    val decision = when {
                        hourly >= 35 -> "ACCEPT"
                        hourly >= 30 -> "CONSIDER"
                        else -> "DECLINE"
                    }
                    "$decision - AUD %.2f/hour".format(hourly)
                }
            }
        })

        layout.addView(result)

        layout.addView(Button(this).apply {
            text = "Refresh Dasher Notification"
            setOnClickListener {
                val prefs = getSharedPreferences("dasher", MODE_PRIVATE)
                status.text = prefs.getString(
                    "last", "No Dasher notification detected"
                )
            }
        })

        layout.addView(TextView(this).apply {
            text = "Privacy: This V1 does not record your screen."
            textSize = 12f
        })

        val scroll = ScrollView(this)
        scroll.addView(layout)
        setContentView(scroll)
    }
}
