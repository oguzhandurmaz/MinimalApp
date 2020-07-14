package com.example.minimalistapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.max

class BroadReceiver: BroadcastReceiver() {
    /*override fun onActivityDestroy(context: Context) {
        Log.i("Receiver","onActivityDestroy")

        val prefs = context.getSharedPreferences("count",MODE_PRIVATE)
        prefs?.let {
            StaticVariables.count = it.getInt("count", 0)
            Log.i("Receiver","${StaticVariables.count}")
        }

    }*/

    //var getCount: Boolean = true
    var receiverCount = 0
    lateinit var sqlite: SQLiteDataBaseHelper

    var hourOn: Int = 0
    var minOn: Int = 0
    var secOn: Int = 0

    var firstScreenOn: Boolean = false


    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent?.action
        //Log.i("Receiver",action)

        val calendar = Calendar.getInstance()

        sqlite = SQLiteDataBaseHelper.ISqLiteInterface.getSQLite(context)

        /*if(StaticVariables.isGetSaveDoc){
            StaticVariables.count = getCount(context)
            StaticVariables.isGetSaveDoc = false
        }*/


        when(action){

            Intent.ACTION_SCREEN_ON ->{
                firstScreenOn = true
                hourOn = calendar.get(Calendar.HOUR_OF_DAY)
                minOn = calendar.get(Calendar.MINUTE)
                secOn = calendar.get(Calendar.SECOND)

                Log.i("ScreenOn",hourOn.toString() + minOn.toString())
                saveCount(context,getCount(context)+1)

                Log.i("Receiver","${getCount(context)}")

                //Intent ForeGround Service
                val intentService = Intent(context,Service::class.java)
                intent.putExtra("count",getCount(context))
                context?.startService(intentService)


                Log.i("Receiver","Screen ON")
                //Toast.makeText(context,"Ekran Bugün ${countScreenOn} Kez Açıldı",Toast.LENGTH_SHORT).show()
            }


            Intent.ACTION_SCREEN_OFF ->{
                 Log.i("Receiver","Screen OFF")
                if(firstScreenOn){
                    val hourOff = calendar.get(Calendar.HOUR_OF_DAY)
                    val minOff = calendar.get(Calendar.MINUTE)
                    val secOff = calendar.get(Calendar.SECOND)
                    val time = (hourOff - hourOn)*60 + (minOff - minOn) + abs((secOff - secOn)/60) // Dakika Türünden

                    saveAllTime(context,time + getAllTime(context))
                    if(getMaxTime(context) < time){
                        saveMaxTime(context,time)
                    }
                }


            }
            Intent.ACTION_TIME_TICK -> {
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val min = calendar.get(Calendar.MINUTE)
                if(hour.toString().equals("0") && min.toString().equals("0")){
                    Thread{
                        sqlite.insertCount(getCount(context),StaticVariables.date,getMaxTime(context),getAllTime(context))

                        StaticVariables.date = SimpleDateFormat("dd-MM-yyyy",Locale.getDefault()).format(Date())

                        resetCount(context)
                        resetMaxTime(context)
                        resetAllTime(context)
                    }.start()
                }
            }

            }
        }
        fun getCount(context: Context?): Int{
            val prefs = context?.getSharedPreferences("SaveDoc", MODE_PRIVATE)
            var getC = 0
            prefs?.apply {
                getC = this.getInt("screenOnCount", 0)
            }
            return getC
        }
        fun saveCount(context: Context?, value: Int){
            val prefs = context?.getSharedPreferences("SaveDoc", MODE_PRIVATE)
            prefs?.apply {
                val editor = this.edit()
                editor.putInt("screenOnCount",value)
                editor.apply()
            }
        }
        fun resetCount(context: Context?){
            val prefs = context?.getSharedPreferences("SaveDoc", MODE_PRIVATE)
            prefs?.apply {
                val editor = this.edit()
                editor.putInt("screenOnCount",0)
                editor.apply()
            }
        }
        fun saveMaxTime(context: Context?,maxTime: Int){
            val prefs = context?.getSharedPreferences("SaveDoc", MODE_PRIVATE)
            prefs?.apply {
                val editor = this.edit()
                editor.putInt("maxTime", maxTime)
                editor.apply()
            }
        }
        fun getMaxTime(context: Context?): Int{
            var maxTime = 0
            val prefs = context?.getSharedPreferences("SaveDoc", MODE_PRIVATE)
            prefs?.apply {
                maxTime = this.getInt("maxTime", 0)
            }
            return maxTime
        }
        fun resetMaxTime(context: Context?){
            val prefs = context?.getSharedPreferences("SaveDoc", MODE_PRIVATE)
            prefs?.apply {
                val editor = this.edit()
                editor.putInt("maxTime", 0)
                editor.apply()
            }
        }
        fun saveAllTime(context: Context?,time: Int){
            val prefs = context?.getSharedPreferences("SaveDoc", MODE_PRIVATE)
            prefs?.apply {
                val editor = this.edit()
                editor.putInt("allTime",time)
                editor.apply()
            }
        }
        fun getAllTime(context: Context?): Int{
        var allTime = 0
        val prefs = context?.getSharedPreferences("SaveDoc", MODE_PRIVATE)
        prefs?.apply {
            allTime = this.getInt("allTime", 0)
        }
        return allTime
        }
        fun resetAllTime(context: Context?){
            val prefs = context?.getSharedPreferences("SaveDoc", MODE_PRIVATE)
            prefs?.apply {
                val editor = this.edit()
                editor.putInt("allTime", 0)
                editor.apply()
            }
        }


    }