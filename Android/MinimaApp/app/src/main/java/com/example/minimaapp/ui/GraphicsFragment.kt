package com.example.minimaapp.ui


import android.os.Bundle
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
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

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
                setTimeChart(data)
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
        val aXis = binding.timeBarChart.xAxis
        axLeft.isEnabled = false
        axRight.isEnabled = false
        aXis.setDrawGridLines(false)
        aXis.axisLineColor = android.R.color.transparent
        aXis.textColor = ContextCompat.getColor(requireContext(),R.color.blue)
        aXis.position = XAxis.XAxisPosition.BOTTOM


        binding.timeBarChart.description.isEnabled = false
        binding.timeBarChart.setScaleEnabled(false)
    }

    private fun initCountChart() {
        val axLeft = binding.countBarChart.axisLeft
        val axRight = binding.countBarChart.axisRight
        val aXis = binding.countBarChart.xAxis
        axLeft.isEnabled = false
        axRight.isEnabled =false
        aXis.setDrawGridLines(false)
        aXis.axisLineColor = android.R.color.transparent
        aXis.textColor = ContextCompat.getColor(requireContext(),R.color.purple)
        aXis.position = XAxis.XAxisPosition.BOTTOM


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
        //Bar üstündeki değerleri 0.0 dan 0 yapmak için.
        barData.setValueFormatter(ValueFormatter())

        //X axise tarih değerleri vermek için.
        binding.countBarChart.xAxis.valueFormatter = AxisValuesFormatter()

        binding.countBarChart.isDragEnabled = false
        binding.countBarChart.data = barData

        binding.countBarChart.animateY(700)
        binding.countBarChart.invalidate()

    }
    private fun setTimeChart(data: List<Count>){
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
        //Bar üstündeki değerleri 0.0 dan 0 yapmak için.
        barData.setValueFormatter(ValueFormatter())

        //X axise tarih değerleri vermek için.
        binding.timeBarChart.xAxis.valueFormatter = AxisValuesFormatter()

        binding.timeBarChart.isDragEnabled = false
        binding.timeBarChart.data = barData

        binding.timeBarChart.animateY(700)
        binding.timeBarChart.invalidate()
    }

    private inner class ValueFormatter: IndexAxisValueFormatter(){
        override fun getFormattedValue(value: Float): String {
            return value.toInt().toString()
        }
    }
    private inner class AxisValuesFormatter: IndexAxisValueFormatter(){
        override fun getFormattedValue(value: Float): String {
            if(viewModel.countDataSeven.value?.size!! - 1 < value.toInt()){
                return "0-0"
            }
            val date = viewModel.countDataSeven.value?.get(value.toInt())?.date?.split("-")
            return "${date?.get(0)}-${date?.get(1)}"
        }
    }


}
