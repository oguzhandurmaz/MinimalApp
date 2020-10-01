package com.example.minimaapp.utils

import android.content.Context
import com.example.minimaapp.data.CountRoomDatabase
import com.example.minimaapp.data.table.Count
import com.example.minimaapp.di.DaggerAppComponent
import com.example.minimaapp.repo.CountRepository
import dagger.android.DaggerBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

class Utils {


    companion object {

       /* @Inject
        lateinit var countRepository: CountRepository*/

        fun getDifferences(screenOn: Long,screenOff: Long): Int{
            /*
                Return second.
             */
            return ((screenOff-screenOn)/1000).toInt()

        }

        fun saveScreenOnCount(context: Context?, count: Int) {
            context?.getSharedPreferences("minimal_app", Context.MODE_PRIVATE)?.apply {
                edit()
                    .putInt("count", count)
                    .apply()
            }
        }

        fun getScreenOnCount(context: Context?): Int {
            return context?.getSharedPreferences("minimal_app", Context.MODE_PRIVATE)?.run {
                getInt("count", 0)
            } ?: 0

        }

        fun getScreenOnTime(context: Context?): Int {
            return context?.getSharedPreferences("minimal_app", Context.MODE_PRIVATE)?.run {
                getInt("time", 0)
            } ?: 0
        }

        fun saveScreenOnTime(context: Context?, time: Int) {
            context?.getSharedPreferences("minimal_app", Context.MODE_PRIVATE)?.apply {
                edit()
                    .putInt("time", time)
                    .apply()
            }
        }

        fun resetValues(context: Context?) {
            context?.apply {
                val countShared = getSharedPreferences("minimal_app", Context.MODE_PRIVATE)
                val timeShared = getSharedPreferences("minimal_app", Context.MODE_PRIVATE)

                val editCount = countShared.edit()
                val editTime = timeShared.edit()

                editCount.putInt("count", 0)
                editTime.putInt("time", 0)

                editCount.apply()
                editTime.apply()

                //Static Value Reset
                //StaticVariables.time = 0
            }
        }

        fun saveServiceState(context: Context?, state: Boolean) {
            context?.getSharedPreferences("minimal_app", Context.MODE_PRIVATE)?.apply {
                edit()
                    .putBoolean("service_state", state)
                    .apply()
            }
        }

        fun getServiceState(context: Context?): Boolean {
            return context?.getSharedPreferences("minimal_app", Context.MODE_PRIVATE)?.run {
                getBoolean("service_state", false)
            } ?: false
        }

        fun saveDate(context: Context?,date: String){
            context?.getSharedPreferences("minimal_app", Context.MODE_PRIVATE)?.apply {
                edit()
                    .putString("date", date)
                    .apply()
            }
        }
        fun getDate(context: Context?): String{
            return context?.getSharedPreferences("minimal_app",Context.MODE_PRIVATE)?.run {
                getString("date","01-01-1990")
            }?: "01-01-1990"
        }
    }


}