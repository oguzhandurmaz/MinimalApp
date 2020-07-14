package com.example.minimalistapp

import android.content.Intent
import android.content.IntentFilter


val intentFilter = IntentFilter()
val broadReceiver = BroadReceiver()

object BroadSingleton {
    val broadReceiver by lazy {
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)

    }
}