package com.example.minimaapp.ui


import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textservice.TextServicesManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.minimaapp.CountService
import com.example.minimaapp.data.table.Count
import com.example.minimaapp.viewmodel.CountViewModel
import com.example.minimaapp.adapter.RecyclerViewRegisterAdapter
import com.example.minimaapp.databinding.FragmentCountBinding

/**
 * A simple [Fragment] subclass.
 */
class CountFragment : Fragment() {

    private lateinit var binding: FragmentCountBinding

    //ViewModel
    private lateinit var viewModel: CountViewModel
    private lateinit var viewModelFactory: CountViewModel.CountViewModelFactory

    private var isServiceRunning = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCountBinding.inflate(inflater)

        //viewModel
        viewModelFactory =
            CountViewModel.CountViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(CountViewModel::class.java)

        val adapter = RecyclerViewRegisterAdapter()
        binding.recyclerview.adapter = adapter


        viewModel.countDataSeven.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.submitList(it) }
        })

        //Check Service Running

        isServiceRunning = getServiceState()

        if(isServiceRunning){
            binding.btnStart.text = "Stop"
        }else{
            binding.btnStart.text = "Start"
        }

        binding.btnStart.setOnClickListener {
           // viewModel.insert(Count(0, "20-20-1010", 10, 20))
            isServiceRunning = getServiceState()
            val intent = Intent(requireActivity(),CountService::class.java)
            if(isServiceRunning){
               requireActivity().stopService(intent)
                binding.btnStart.text = "Start"

            }else{
                ContextCompat.startForegroundService(requireContext(),intent)
                binding.btnStart.text = "Stop"
            }
        }



        return binding.root
    }

    private fun getServiceState(): Boolean{
        val shared = requireActivity().getSharedPreferences("minimal_app",Context.MODE_PRIVATE)
        return shared.getBoolean("service_state",false)
    }


}
