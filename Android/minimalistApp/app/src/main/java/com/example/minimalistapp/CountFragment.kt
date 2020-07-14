package com.example.minimalistapp


import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.minimalistapp.databinding.FragmentCountBinding
import kotlinx.android.synthetic.main.fragment_count.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class CountFragment : Fragment() {
    lateinit var binding: FragmentCountBinding

    lateinit var wakeLock: PowerManager.WakeLock

    companion object{
        lateinit var viewPager: ViewPager2
    }

    private val WAKE_LOCK_REQUEST = 0
    val mService = Service()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        binding = FragmentCountBinding.inflate(inflater)

        viewPager = binding.viewPager2
        binding.viewPager2.adapter = ViewPagerAdapter(activity!!.supportFragmentManager,lifecycle)
        //binding.viewPager2.setPageTransformer(ZoomOutPageTransformer())


        // Inflate the layout for this fragment
        return binding.root
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

    private inner class ViewPagerAdapter(fragmentManager: FragmentManager,lifeCycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifeCycle){
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            var fragments = arrayListOf<Fragment>()
            fragments.add(ScreenOneFragment())
            fragments.add(ScreenTwoFragment())
            return fragments[position]

        }

    }

}
