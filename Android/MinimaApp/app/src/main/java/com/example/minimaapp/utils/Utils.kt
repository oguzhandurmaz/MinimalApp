package com.example.minimaapp.utils

import android.content.Context
import com.example.minimaapp.data.CountRoomDatabase
import com.example.minimaapp.data.table.Count
import com.example.minimaapp.repo.CountRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

class Utils {
    companion object {

        //Return Differences in Second Type
        fun getDifferencesOfTime(
            hour1: Int,
            min1: Int,
            sec1: Int,
            hour2: Int,
            min2: Int,
            sec2: Int
        ): Int {
            return abs((hour1 * 360 + min1 * 60 + sec1) - (hour2 * 360 + min2 * 60 + sec2))

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
                StaticVariables.time = 0
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

        fun saveAndResetValues(context: Context?) {
            context?.apply {
                //Database'e Verileri Kaydet
                val countDao = CountRoomDatabase.getDataBase(this).countDao()
                val countRepository = CountRepository(countDao)
                //Save Values
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Default) {
                        countRepository.insert(
                            Count(
                                0,
                                getDate(context),
                                getScreenOnCount(this@apply),
                                getScreenOnTime(this@apply)
                            )
                        )
                    }
                    //Reset Values
                    resetValues(this@apply)
                }
            }

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