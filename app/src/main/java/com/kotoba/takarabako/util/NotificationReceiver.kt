package com.kotoba.takarabako.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.kotoba.takarabako.MainActivity
import com.kotoba.takarabako.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            NotificationHelper.rescheduleFromPrefs(context)
            return
        }

        val hour = intent.getIntExtra("hour", 9)
        val minute = intent.getIntExtra("minute", 0)

        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val tapPendingIntent = PendingIntent.getActivity(
            context, 0, launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("일본어명언집")
            .setContentText("오늘의 일본어 학습을 시작하세요!")
            .setAutoCancel(true)
            .setContentIntent(tapPendingIntent)
            .build()
        nm.notify(NotificationHelper.NOTIFICATION_ID, notification)

        NotificationHelper.schedule(context, hour, minute)
    }
}
