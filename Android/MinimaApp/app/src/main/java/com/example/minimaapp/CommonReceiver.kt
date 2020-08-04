package com.example.minimaapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.adapters.CalendarViewBindingAdapter.setDate
import com.example.minimaapp.utils.StaticVariables
import com.example.minimaapp.utils.Utils
import com.example.minimaapp.utils.Utils.Companion.getDate
import com.example.minimaapp.utils.Utils.Companion.getScreenOnCount
import com.example.minimaapp.utils.Utils.Companion.getScreenOnTime
import com.example.minimaapp.utils.Utils.Companion.getServiceState
import com.example.minimaapp.utils.Utils.Companion.resetValues
import com.example.minimaapp.utils.Utils.Companion.saveAndResetValues
import com.example.minimaapp.utils.Utils.Companion.saveDate
import com.example.minimaapp.utils.Utils.Companion.saveScreenOnCount
import com.example.minimaapp.utils.Utils.Companion.saveScreenOnTime
import java.text.SimpleDateFormat
import java.util.*

class CommonReceiver : BroadcastReceiver() {



    private var hourOn = 0
    private var minOn = 0
    private var secOn = 0

    private var isFirstScreenOn = false

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        val calendar = Calendar.getInstance()

        when (action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                context?.apply {
                    //Service Durdurulmuşsa Çalıştırma
                    if (getServiceState(this)) {
                        //Kapatıldığı zamanla açıldığı zaman aynı ise kaldığı yerden devam et. Değilse baştan başla
                        if (getDate(context) == SimpleDateFormat(
                                "dd-MM-yyyy",
                                Locale.getDefault()
                            ).format(Date())
                        ) {
                            //Set Time Variable for Show TextView
                            StaticVariables.time = getScreenOnTime(this)
                            Intent(this, CountService::class.java).let {
                                it.putExtra("count", getScreenOnCount(this))
                                ContextCompat.startForegroundService(
                                    this,
                                    it
                                )
                            }
                        } else {
                            //Verileri Kaydet ve Resetle - Yeni güne başla
                            saveAndResetValues(this)
                            saveDate(context,SimpleDateFormat(
                                "dd-MM-yyyy",
                                Locale.getDefault()
                            ).format(Date()))
                            StaticVariables.date = SimpleDateFormat(
                                "dd-MM-yyyy",
                                Locale.getDefault()
                            ).format(Date())

                            ContextCompat.startForegroundService(
                                this,
                                Intent(this, CountService::class.java)
                            )
                        }
                    }
                }
            }
            Intent.ACTION_SCREEN_ON -> {
                hourOn = calendar.get(Calendar.HOUR_OF_DAY)
                minOn = calendar.get(Calendar.MINUTE)
                secOn = calendar.get(Calendar.SECOND)

                saveScreenOnCount(context, getScreenOnCount(context) + 1)

                sendCountToService(context)

                Log.d("Minimal", "Screen On")

                if (!isFirstScreenOn) isFirstScreenOn = true


                //saveValues(context)

            }
            Intent.ACTION_SCREEN_OFF -> {
                val hourOff = calendar.get(Calendar.HOUR_OF_DAY)
                val minOff = calendar.get(Calendar.MINUTE)
                val secOff = calendar.get(Calendar.SECOND)

                Log.d("Minimal","hourOn: $hourOn,minOn: $minOn, secOn: $secOn\nhourOff: $hourOff,minOff: $minOff, secOff: $secOff")

                if (isFirstScreenOn) {
                    val screenOnTime =
                        Utils.getDifferencesOfTime(hourOn, minOn, secOn, hourOff, minOff, secOff)

                    //En uzun açık kalma süresi bir öncekinden büyükse kaydet.
                    context.apply {
                        if (screenOnTime > getScreenOnTime(this)) {
                            saveScreenOnTime(this, screenOnTime)
                            StaticVariables.time = screenOnTime
                        }
                    }

                }


            }
            Intent.ACTION_TIME_TICK -> {
                //Saat 00.00 ise bir sonraki gün ise
                if (calendar.get(Calendar.HOUR_OF_DAY).toString() == "0" && calendar.get(Calendar.MINUTE).toString() == "0") {

                    context?.apply {
                        //save And Reset Values
                        saveAndResetValues(this)

                        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                        //Tarihi Değiştir
                        StaticVariables.date = date
                        saveDate(context,date)

                    }
                }
            }
        }
    }

    private fun sendCountToService(context: Context?) {
        context?.let {
            Intent(it, CountService::class.java).apply {
                putExtra("count", getScreenOnCount(it))
                ContextCompat.startForegroundService(it, this)
            }

        }
    }
}