package com.example.minimaapp.ui


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.example.minimaapp.CountService
import com.example.minimaapp.R
import com.example.minimaapp.adapter.RecyclerViewRegisterAdapter
import com.example.minimaapp.data.table.Count
import com.example.minimaapp.databinding.FragmentCountBinding
import com.example.minimaapp.utils.StaticVariables
import com.example.minimaapp.utils.Utils.Companion.getDate
import com.example.minimaapp.utils.Utils.Companion.getScreenOnCount
import com.example.minimaapp.utils.Utils.Companion.getScreenOnTime
import com.example.minimaapp.utils.Utils.Companion.getServiceState
import com.example.minimaapp.utils.Utils.Companion.saveAndResetValues
import com.example.minimaapp.utils.Utils.Companion.saveDate
import com.example.minimaapp.utils.Utils.Companion.saveServiceState
import com.example.minimaapp.viewmodel.CountViewModel
import com.example.minimaapp.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class CountFragment : DaggerFragment() {

    private var _binding: FragmentCountBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var adapter: RecyclerViewRegisterAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory


    //ViewModel
    private lateinit var viewModel: CountViewModel
    //private lateinit var viewModelFactory: CountViewModel.CountViewModelFactory

    private var isServiceRunning = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCountBinding.inflate(inflater)

        //viewModel
        viewModel = ViewModelProvider(this, viewModelFactory).get(CountViewModel::class.java)

        //val adapter = RecyclerViewRegisterAdapter()
        binding.recyclerview.adapter = adapter


        //Set Date
        //StaticVariables.date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        saveDate(
            requireContext(),
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        )

        //Set Count, Date, Time Values to TextViews
        setValuesToTextViews()

        viewModel.countDataSeven.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.submitList(it) }
        })

        //Check Service Running

        isServiceRunning = getServiceState(requireContext())

        if (isServiceRunning) {
            binding.btnStart.text = getString(R.string.stop)
        } else {
            binding.btnStart.text = getString(R.string.start)
        }
        

        binding.btnStart.setOnClickListener {
            //viewModel.insert(Count(0, "20-20-1010", 10, 20))
            isServiceRunning = getServiceState(requireContext())

            val intent = Intent(requireActivity(), CountService::class.java)
            if (getDate(requireContext()) == SimpleDateFormat(
                    "dd-MM-yyyy",
                    Locale.getDefault()
                ).format(
                    Date()
                )
            ) {
                intent.putExtra("count", getScreenOnCount(requireContext()))
            } else {
                // Önceki verileri kaydedip sıfırla - Bir sonraki güne başla
                saveAndResetValues(requireContext())
                //Save Date
                saveDate(
                    requireContext(), SimpleDateFormat(
                        "dd-MM-yyyy",
                        Locale.getDefault()
                    ).format(Date())
                )

             /*   StaticVariables.date == SimpleDateFormat(
                    "dd-MM-yyyy",
                    Locale.getDefault()
                ).format(Date())*/
            }
            if (isServiceRunning) {
                requireActivity().stopService(intent)
                binding.btnStart.text = getString(R.string.start)

            } else {
                ContextCompat.startForegroundService(requireContext(), intent)
                binding.btnStart.text = getString(R.string.stop)
            }
        }



        return binding.root
    }


    private fun setValuesToTextViews() {
        val count = "${getString(R.string.count)}\n${getScreenOnCount(context)}"
        val time = "${getString(R.string.time)}\n${getScreenOnTime(context)}"
        val temp = getDate(requireContext()).split("-")
        val date = "${getString(R.string.date)}\n${temp[0]}-${temp[1]}\n${temp[2]}"
        binding.textCount.text = count
        binding.textTime.text = time
        binding.textDate.text = date
    }

    override fun onResume() {
        super.onResume()
        setValuesToTextViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerview.adapter = null
        _binding = null
    }


}
