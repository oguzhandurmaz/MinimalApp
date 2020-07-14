package com.example.minimalistapp

import android.app.*
import android.app.Service
import android.content.*
import android.content.res.Configuration
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationCompat
import androidx.core.view.MotionEventCompat
import java.text.SimpleDateFormat
import java.util.*

class Service : Service() {

    var isRegistered: Boolean = false

    val broadReceiver = BroadReceiver()

    val channelID = "channelID"
    val channelName = "minimalChannel"
    val notificationID = 1


    override fun onBind(intent: Intent?): IBinder? {
        Log.i("Service","Bind")
        return null
    }

    override fun onCreate() {
        Log.i("Service","onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("Service","Start")

        val count = intent?.getIntExtra("count",getCount()) ?: getCount()
        createNotificationChannel()


        val intent = Intent(this,MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run{
            addNextIntentWithParentStack(intent)
            getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(this,channelID)
            .setContentTitle("Minimal App")
            .setContentText("Screen On Count: $count")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setPriority(NotificationCompat.PRIORITY_LOW)



        saveServiceState(true)


        startForeground(notificationID,notification.build())
        //For Dont call again Receiver
        /*val sharedPreferences = getSharedPreferences("SaveDoc",Context.MODE_PRIVATE)
        isRegistered = sharedPreferences.getBoolean("registerState",false)*/

        if(!isRegistered){
            val intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
            intentFilter.addAction(Intent.ACTION_SCREEN_ON)
            intentFilter.addAction(Intent.ACTION_TIME_TICK)
            intentFilter.priority = 100
            registerReceiver(broadReceiver,intentFilter)
            isRegistered = true
            saveRegister(true)
        }

        StaticVariables.date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())



        //return super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY
    }
    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_MIN).apply {
                enableLights(false)
                enableVibration(false)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    fun saveCount(){
        val sharedPreferencesCount = getSharedPreferences("SaveDoc",Context.MODE_PRIVATE)
        val editor = sharedPreferencesCount.edit()
        editor.putInt("screenOnCount",StaticVariables.count)
        editor.apply()
    }
    fun saveRegister(register: Boolean){
        val sharedPreferencesRegister = getSharedPreferences("SaveDoc", Context.MODE_PRIVATE)
        val editor = sharedPreferencesRegister.edit()
        editor.putBoolean("registerState",register)
        editor.apply()
    }
    fun saveServiceState(state: Boolean){
        val pref = getSharedPreferences("SaveDoc",Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("serviceState",state)
        editor.apply()
    }

    override fun stopService(name: Intent?): Boolean {
        Log.i("Receiver","Stop")
        //unregisterReceiver(broadReceiver)
        saveServiceState(false)
        saveRegister(false)
        return super.stopService(name)
    }

    override fun onDestroy() {
        Log.i("Service","Destroy")
        saveRegister(false)
        saveServiceState(false)
        if(isRegistered){
            unregisterReceiver(broadReceiver)
        }

        //wakeLock.release()
        /*val intent = Intent(".Service")
        sendBroadcast(intent)*/

        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.i("Receiver","Task")
        //saveCount()
        saveRegister(false)
        saveServiceState(false)
        super.onTaskRemoved(rootIntent)
    }

    override fun onTrimMemory(level: Int) {
        //saveCount()
        Log.i("Receiver","onTrimMemory")
        super.onTrimMemory(level)
    }

    override fun onLowMemory() {
        Log.i("Receiver","onLowMemory")
        //saveCount()
        super.onLowMemory()
    }
    fun getCount(): Int{
        val prefs = getSharedPreferences("SaveDoc", Context.MODE_PRIVATE)
        var getC = 0
        prefs?.apply {
            getC = this.getInt("screenOnCount", 0)
        }
        return getC
    }

}