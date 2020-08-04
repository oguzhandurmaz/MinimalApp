package com.example.minimaapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.minimaapp.utils.StaticVariables
import com.example.minimaapp.utils.Utils.Companion.saveDate
import com.example.minimaapp.utils.Utils.Companion.saveServiceState
import java.text.SimpleDateFormat
import java.util.*

class CountService : Service() {

    private val countReceiver = CommonReceiver()

    private var isReceiverRegistered = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val count = intent?.getIntExtra("count",0) ?: 0


            createNotificationChannel()

        val openAppIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(openAppIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(this, "channelId")
            .setContentTitle("Minimal App")
            .setContentText("Screen On Count $count")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_image)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        startForeground(1, notification.build())
        saveServiceState(this,true)

        //RegisterReceiver
        if (!isReceiverRegistered) {

            val intentFilter = IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_ON)
                addAction(Intent.ACTION_SCREEN_OFF)
                addAction(Intent.ACTION_TIME_TICK)
            }

            registerReceiver(countReceiver, intentFilter)

            //Save Date
            StaticVariables.date =
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            saveDate(this,SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()))

            isReceiverRegistered = true
        }

        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channelId",
                "channelName",
                NotificationManager.IMPORTANCE_MIN
            ).apply {
                enableVibration(false)
                enableLights(false)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        saveServiceState(this,false)

        if (isReceiverRegistered) {
            unregisterReceiver(countReceiver)
            isReceiverRegistered = false
        }
    }

    /*private fun saveServiceState(state: Boolean) {
        val shared = getSharedPreferences("minimal_app", Context.MODE_PRIVATE)
        val editor = shared.edit()
        editor.putBoolean("service_state", state)
        editor.apply()
    }*/
}