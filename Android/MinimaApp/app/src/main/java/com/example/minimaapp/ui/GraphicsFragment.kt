package com.example.minimaapp.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.minimaapp.R
import com.example.minimaapp.data.table.Count
import com.example.minimaapp.databinding.FragmentGraphicsBinding
import com.example.minimaapp.viewmodel.GraphicsFragmentViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

/**
 * A simple [Fragment] subclass.
 */
class GraphicsFragment : Fragment() {

    private lateinit var binding: FragmentGraphicsBinding


    private lateinit var viewModel: GraphicsFragmentViewModel
    private lateinit var viewModelFactory: GraphicsFragmentViewModel.GraphicsFragmentViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGraphicsBinding.inflate(inflater)

        viewModelFactory = GraphicsFragmentViewModel.GraphicsFragmentViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(GraphicsFragmentViewModel::class.java)

        viewModel.countDataSeven.observe(viewLifecycleOwner, Observer {data ->
            data?.let {
                setCountChart(data)
                setTimechart(data)
            }
        })

        initCountChart()

        initTimeChart()




        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initTimeChart() {
        val axLeft = binding.timeBarChart.axisLeft
        val axRight = binding.timeBarChart.axisRight

        axLeft.isEnabled = false
        axRight.isEnabled = false
        /*axLeft.setDrawGridLines(false)
        axLeft.setDrawLabels(false)

        axRight.setDrawGridLines(false)
        axRight.setDrawLabels(false)
*/
        val aXis = binding.timeBarChart.xAxis
        aXis.axisLineColor = android.R.color.transparent
        aXis.textColor = ContextCompat.getColor(requireContext(),R.color.blue)
        aXis.setDrawGridLines(false)
        aXis.position = XAxis.XAxisPosition.BOTTOM

        binding.timeBarChart.setMaxVisibleValueCount(7)
        binding.timeBarChart.description.isEnabled = false
        binding.timeBarChart.setScaleEnabled(false)
    }

    private fun initCountChart() {
        val axLeft = binding.countBarChart.axisLeft
        val axRight = binding.countBarChart.axisRight

        axLeft.isEnabled = false
        axRight.isEnabled = false
        /*axLeft.setDrawGridLines(false)
        axLeft.setDrawLabels(false)

        axRight.setDrawGridLines(false)
        axRight.setDrawLabels(false)*/

        val aXis = binding.countBarChart.xAxis
        aXis.axisLineColor = android.R.color.transparent
        aXis.textColor = ContextCompat.getColor(requireContext(),R.color.purple)
        aXis.setDrawGridLines(false)
        aXis.position = XAxis.XAxisPosition.BOTTOM


        binding.countBarChart.setMaxVisibleValueCount(7)
        binding.countBarChart.description.isEnabled = false
        binding.countBarChart.setScaleEnabled(false)

    }

    private fun setCountChart(data: List<Count>){
        val entries = ArrayList<BarEntry>()
        for(i in data.indices){
            entries.add(BarEntry(i.toFloat(),data[i].count.toFloat()))
        }
        //Grafikte düzgün bir görüntü görmek için. Her zaman 7 değer olacak boş olanlar 0 olacak.
        if(entries.size < 7){
            while(entries.size < 7){
                entries.add(BarEntry((entries.size+1).toFloat(),0f))
            }
        }
        val barDataSet = BarDataSet(entries,"Counts")
        barDataSet.color = ContextCompat.getColor(requireContext(),R.color.purple)
        val barData = BarData(barDataSet)

        binding.countBarChart.isDragEnabled = false
        binding.countBarChart.data = barData
        binding.countBarChart.invalidate()

    }
    private fun setTimechart(data: List<Count>){
        val entries = ArrayList<BarEntry>()
        for(i in data.indices){
            entries.add(BarEntry(i.toFloat(),data[i].time.toFloat()))
        }
        //Grafikte düzgün bir görüntü görmek için. Her zaman 7 değer olacak boş olanlar 0 olacak.
        if(entries.size < 7){
            while(entries.size < 7){
                entries.add(BarEntry((entries.size+1).toFloat(),0f))
            }
        }
        val barDataSet = BarDataSet(entries,"Time")
        barDataSet.color = ContextCompat.getColor(requireContext(),R.color.blue)
        val barData = BarData(barDataSet)

        binding.timeBarChart.isDragEnabled = false
        binding.timeBarChart.data = barData
        binding.timeBarChart.invalidate()
    }


}
