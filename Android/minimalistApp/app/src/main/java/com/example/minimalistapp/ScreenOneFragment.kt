package com.example.minimalistapp


import android.app.ActivityManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.NetworkRequest
import android.os.Bundle
import android.os.PowerManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.JobIntentService
import androidx.core.content.ContextCompat
import com.example.minimalistapp.databinding.FragmentScreenOneBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ScreenOneFragment : Fragment() {

    lateinit var binding: FragmentScreenOneBinding

    lateinit var wakeLock: PowerManager.WakeLock

    val mService = Service()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentScreenOneBinding.inflate(inflater)

        updateTexts()


        binding.btnStartService.text = if(isServiceAlive()) resources.getString(R.string.stopService)
                                        else resources.getString(R.string.startService)

        val sqLite = SQLiteDataBaseHelper.ISqLiteInterface.getSQLite(binding.root.context)
        val registerList = sqLite.selectCount()


        binding.btnStartService.setOnClickListener {
            /*val serviceState = pref.getBoolean("serviceState",false)*/
            val intent = Intent(binding.root.context,Service::class.java)
            //jobScheduler.schedule(info)
            //JobIntentService.enqueueWork(binding.root.context,Service::class.java,10,intent)
            if(isServiceAlive()){
                binding.root.context.stopService(intent)
                binding.btnStartService.text = resources.getString(R.string.startService)
            }else{
                ContextCompat.startForegroundService(binding.root.context,intent)
                //wakeLockOnOff(true)
                binding.btnStartService.text = resources.getString(R.string.stopService)
            }

        }

        val recyclerAdapter = SqlRecyclerAdapter(registerList)
        binding.recyclerView.adapter = recyclerAdapter


        // Inflate the layout for this fragment
        return binding.root
    }
    fun updateTexts(){
        binding.txtCount.text = getCount().toString()
        binding.txtMaxTime.text = getMaxTime().toString()
        binding.txtDate.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    }
    fun getCount(): Int{
        val prefs = binding.root.context.getSharedPreferences("SaveDoc", Context.MODE_PRIVATE)
        return prefs.getInt("screenOnCount",0)
    }
    fun getMaxTime(): Int{
        val prefs = binding.root.context.getSharedPreferences("SaveDoc", Context.MODE_PRIVATE)
        return prefs.getInt("maxTime",0)
    }
    fun isServiceAlive(): Boolean{
        val manager = activity?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for(services in manager.getRunningServices(Integer.MAX_VALUE)){
            if(mService.javaClass.name.equals(services.service.className)){
                return true
            }
        }
        return false
    }
    /*fun wakeLockOnOff(state: Boolean){
        if(ContextCompat.checkSelfPermission(binding.root.context,android.Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED){
            if(state){
                if(!wakeLock.isHeld){
                    wakeLock.acquire()
                }

            }else{
                if(wakeLock.isHeld){
                    wakeLock.release()
                }

            }
        }
    }*/


}
