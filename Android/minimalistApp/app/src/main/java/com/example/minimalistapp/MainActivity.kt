package com.example.minimalistapp

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    val broadReceiver = BroadReceiver()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.actionBar))

        val navController = findNavController(R.id.hostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)


        /*val wakeLock: PowerManager.WakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyApp::MyWakelockTag")
        }
        if(!wakeLock.isHeld)  wakeLock.acquire()*/


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.hostFragment)
        return NavigationUI.navigateUp(navController,drawerLayout)
    }

    override fun onDestroy() {
        //Uygulamadan çıkarken mevcut durumu kaydet
        /*val prefs = applicationContext.getSharedPreferences("SaveDoc", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("screenOnCount",StaticVariables.count)
        editor.apply()*/
        super.onDestroy()
    }

    override fun onBackPressed() {
        if(CountFragment.viewPager.currentItem == 0){
            super.onBackPressed()
        }else{
            CountFragment.viewPager.currentItem = CountFragment.viewPager.currentItem -1
        }


    }


}
