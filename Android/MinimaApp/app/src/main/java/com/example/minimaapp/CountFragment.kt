package com.example.minimaapp


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCountBinding.inflate(inflater)

        //viewModel
        viewModelFactory = CountViewModel.CountViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(CountViewModel::class.java)

        val adapter = RecyclerViewRegisterAdapter()
        binding.recyclerview.adapter = adapter


        viewModel.countDataSeven.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.submitList(it) }
        })

        binding.btnStart.setOnClickListener {
            viewModel.insert(Count(0,"20-20-1010",10,20))
        }



        return binding.root
    }


}
