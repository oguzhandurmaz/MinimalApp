package com.example.minimaapp.ui


import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.minimaapp.utils.StaticVariables
import com.example.minimaapp.utils.Utils.Companion.getScreenOnCount
import com.example.minimaapp.utils.Utils.Companion.getScreenOnTime
import com.example.minimaapp.utils.Utils.Companion.getServiceState
import com.example.minimaapp.utils.Utils.Companion.resetValues
import com.example.minimaapp.utils.Utils.Companion.saveAndResetValues
import java.text.SimpleDateFormat
import java.util.*

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
        viewModel = ViewModelProvider(this, viewModelFactory).get(CountViewModel::class.java)

        val adapter = RecyclerViewRegisterAdapter()
        binding.recyclerview.adapter = adapter

        //Set Date
        StaticVariables.date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        //Set Count, Date, Time Values
        val count = "Count\n${getScreenOnCount(context)}"
        val time = "Time\n${getScreenOnTime(context)}"
        val temp = StaticVariables.date.split("-")
        val date = "Date\n${temp[0]}-${temp[1]}\n${temp[2]}"
        binding.textCount.text = count
        binding.textTime.text = time
        binding.textDate.text = date




        viewModel.countDataSeven.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.submitList(it) }
        })

        //Check Service Running

        isServiceRunning = getServiceState(requireContext())

        if (isServiceRunning) {
            binding.btnStart.text = "Stop"
        } else {
            binding.btnStart.text = "Start"
        }

        binding.btnStart.setOnClickListener {
            // viewModel.insert(Count(0, "20-20-1010", 10, 20))
            isServiceRunning = getServiceState(requireContext())

            val intent = Intent(requireActivity(), CountService::class.java)
            if (StaticVariables.date == SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                    Date()
                )
            ) {
                intent.putExtra("count", getScreenOnCount(requireContext()))
            } else {
                // Önceki verileri kaydedip sıfırla - Bir sonraki güne başla
                saveAndResetValues(requireContext())
                StaticVariables.date == SimpleDateFormat(
                    "dd-MM-yyyy",
                    Locale.getDefault()
                ).format(Date())
            }
            if (isServiceRunning) {
                requireActivity().stopService(intent)
                binding.btnStart.text = "Start"

            } else {
                ContextCompat.startForegroundService(requireContext(), intent)
                binding.btnStart.text = "Stop"
            }
        }



        return binding.root
    }


    fun setValuesToTextViews() {
        val count = "Count\n${getScreenOnCount(context)}"
        val time = "Time\n${getScreenOnTime(context)}"
        val temp = StaticVariables.date.split("-")
        val date = "Date\n${temp[0]}-${temp[1]}\n${temp[2]}"
        binding.textCount.text = count
        binding.textTime.text = time
        binding.textDate.text = date
    }

    override fun onResume() {
        super.onResume()
        setValuesToTextViews()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("Minimal", "onDetach")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Minimal", "onStart")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Minimal", "onAttach")
    }


}
