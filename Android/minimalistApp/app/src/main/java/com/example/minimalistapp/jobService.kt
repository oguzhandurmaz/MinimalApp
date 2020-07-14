package com.example.minimalistapp

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.content.IntentFilter
import android.os.CountDownTimer
import android.util.Log

class jobService: JobService() {
    val broadReceiver = BroadReceiver()
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i("jobService","Stop")
        //unregisterReceiver(broadReceiver)
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {


            object: CountDownTimer(10000,1000) {
                override fun onFinish() {
                    Log.i("jobService","Finish")
                    onStartJob(params)
                }

                override fun onTick(millisUntilFinished: Long) {
                    Log.i("jobService","${millisUntilFinished/1000}")
                }

            }.start()
            Log.i("jobService","Start")
            val intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_SCREEN_ON)
            registerReceiver(broadReceiver,intentFilter)


        return true
    }
}