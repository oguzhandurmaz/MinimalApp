package com.example.minimaapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class CountService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        createNotificationChannel()

        val notification = NotificationCompat.Builder(this,"channelId")
            .setContentTitle("Minimal App")
            .setContentText("Screen On Count")
            .setSmallIcon(R.drawable.ic_image)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        startForeground(1,notification.build())
        saveServiceState(true)

        return START_STICKY
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("channelId","channelName",NotificationManager.IMPORTANCE_MIN).apply {
                enableVibration(false)
                enableLights(false)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveServiceState(false)
        Log.d("Minimal","Service: onDestory")
    }

    private fun saveServiceState(state: Boolean){
        val shared = getSharedPreferences("minimal_app", Context.MODE_PRIVATE)
        val editor = shared.edit()
        editor.putBoolean("service_state",state)
        editor.apply()
    }
}