package com.example.minimaapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.adapters.CalendarViewBindingAdapter.setDate
import com.example.minimaapp.data.table.Count
import com.example.minimaapp.repo.CountRepository
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
import dagger.android.AndroidInjection
import dagger.android.DaggerBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CommonReceiver : DaggerBroadcastReceiver() {



    private var hourOn = 0
    private var minOn = 0
    private var secOn = 0

    @Inject
    lateinit var countRepository: CountRepository

    private var screeOnTime: Long = 0L

    private var isFirstScreenOn = false

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this,context)
        super.onReceive(context, intent)

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
                            //StaticVariables.time = getScreenOnTime(this)
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
                           /* StaticVariables.date = SimpleDateFormat(
                                "dd-MM-yyyy",
                                Locale.getDefault()
                            ).format(Date())*/

                            ContextCompat.startForegroundService(
                                this,
                                Intent(this, CountService::class.java)
                            )
                        }
                    }
                }
            }
            Intent.ACTION_SCREEN_ON -> {
                /*hourOn = calendar.get(Calendar.HOUR_OF_DAY)
                minOn = calendar.get(Calendar.MINUTE)
                secOn = calendar.get(Calendar.SECOND)*/

                screeOnTime = System.currentTimeMillis()

                saveScreenOnCount(context, getScreenOnCount(context) + 1)

                sendCountToService(context)

                if (!isFirstScreenOn) isFirstScreenOn = true


                //saveValues(context)

            }
            Intent.ACTION_SCREEN_OFF -> {

                val screenOffTime = System.currentTimeMillis()

                if (isFirstScreenOn) {
                    val timeDiff = Utils.getDifferences(screeOnTime,screenOffTime)

                    //En uzun açık kalma süresi bir öncekinden büyükse kaydet.
                    context.apply {
                        if (timeDiff > getScreenOnTime(this)) {
                            saveScreenOnTime(this, timeDiff)
                            //StaticVariables.time = timeDiff
                        }
                    }

                }


            }
            Intent.ACTION_TIME_TICK -> {
                //Saat 00.00 ise - bir sonraki gün ise
                if (calendar.get(Calendar.HOUR_OF_DAY).toString() == "0" && calendar.get(Calendar.MINUTE).toString() == "0") {

                    //isFirstScreenOn = false
                    //TODO Save to Database
                    val count = Count(
                        0,
                        getDate(context),
                        getScreenOnCount(context),
                        getScreenOnTime(context)
                    )
                    CoroutineScope(IO).launch {
                        countRepository.insert(count)
                    }



                    context?.apply {
                        //save And Reset Values
                        saveAndResetValues(this)

                        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                        //Tarihi Değiştir
                        //StaticVariables.date = date
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