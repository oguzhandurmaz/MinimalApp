package com.example.minimalistapp


import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.minimalistapp.databinding.FragmentScreenTwoBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ScreenTwoFragment : Fragment() {

    lateinit var binding: FragmentScreenTwoBinding

    var listNetworkData = mutableListOf<NetworkInfo>()

    val adapter = NetworkRecyclerAdapter(listNetworkData)

    private var isCountPressed = true
    private var isTimePressed = false
    private var isCountTimePressed = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentScreenTwoBinding.inflate(inflater)
        //TODO Zaman Chartı

        binding.barChart.setNoDataText("No Data")

        val sqLite = SQLiteDataBaseHelper.ISqLiteInterface.getSQLite(binding.root.context)

        binding.bookViewPager2.adapter = adapter

        initChartView()

        initCountChart(sqLite)

        binding.radioCount.setOnClickListener {
            if(!isCountPressed){
                it.isSelected = true
                initCountChart(sqLite)
                isCountPressed = true
                isTimePressed = false
                isCountTimePressed = false
            }
        }

        binding.radioTime.setOnClickListener {
            if(!isTimePressed){
                it.isSelected = true
                initMaxTimeChart(sqLite)
                isTimePressed = true
                isCountPressed = false
                isCountTimePressed = false
            }
        }

        binding.radioCountTime.setOnClickListener {
            if(!isCountTimePressed){
                it.isSelected = true
                initCountMaxTimeChart(sqLite)
                isCountTimePressed = true
                isTimePressed = false
                isCountPressed = false
            }
        }





        CoroutineScope(IO).launch{
            jsoup()

        }



        // Inflate the layout for this fragment
        return binding.root
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Log.d("Back","Pressed")
                isEnabled = false
                activity?.onBackPressed()
            }

        })
    }*/


    override fun onResume() {
        super.onResume()
        Log.i("onCreateView","Resume")

    }

    override fun onPause() {
        super.onPause()
        Log.i("onCreateView","Pause")
    }

    fun initChartView(){
        val axLeft = binding.barChart.axisLeft
        val axRight = binding.barChart.axisRight
        val aXis = binding.barChart.xAxis
        axLeft.setDrawGridLines(false)
        axLeft.setDrawLabels(false)
        aXis.setDrawGridLines(false)
        axRight.setDrawGridLines(false)
        axRight.setDrawLabels(false)
        aXis.position = XAxis.XAxisPosition.BOTTOM
        binding.barChart.description.isEnabled = false
        binding.barChart.legend.isEnabled = true
        binding.barChart.setScaleEnabled(false)
        binding.barChart.animateY(700)
        binding.barChart.setNoDataText("No Saved Data Yet")


    }

    fun initMaxTimeChart(sqlObject: SQLiteDataBaseHelper){

        val barDataSet = sqlObject.getMaxTimeForChart()
        barDataSet.color = resources.getColor(R.color.colorLightBlue)
        val barData = BarData(barDataSet)
        barData.setValueFormatter(ValueFormater())
        binding.barChart.data = barData
        binding.barChart.isDragEnabled = false
        val aXis = binding.barChart.xAxis
        aXis.valueFormatter = AxisValuesFormater(sqlObject)
        binding.barChart.animateY(700)

        binding.barChart.invalidate()
    }

    fun initCountChart(sqlObject: SQLiteDataBaseHelper){

        val barDataSet = sqlObject.getCountDataForChart()
        barDataSet.color = resources.getColor(R.color.colorGreen)
        val barData = BarData(barDataSet)
        barData.setValueFormatter(ValueFormater())
        binding.barChart.data = barData
        binding.barChart.isDragEnabled = false
        val aXis = binding.barChart.xAxis
        aXis.valueFormatter = AxisValuesFormater(sqlObject)
        binding.barChart.animateY(700)



        //binding.barChart.invalidate()
    }

    fun initCountMaxTimeChart(sqlObject: SQLiteDataBaseHelper){
        val barDataSet1 = sqlObject.getCountDataForChart()
        barDataSet1.color = resources.getColor(R.color.colorGreen)
        val barDataSet2 = sqlObject.getMaxTimeForChart()
        barDataSet2.color = resources.getColor(R.color.colorLightBlue)
        val barData = BarData(barDataSet1,barDataSet2)
        barData.setValueFormatter(ValueFormater())

        val aXis = binding.barChart.xAxis
        aXis.valueFormatter = AxisValuesFormater(sqlObject)
        binding.barChart.isDragEnabled = true

        binding.barChart.data = barData
       // binding.barChart.groupBars(0.2f,0.2f,0.0f)
        binding.barChart.animateY(700)



        // binding.barChart.invalidate()
    }


    private class AxisValuesFormater(private val sqLite: SQLiteDataBaseHelper): IndexAxisValueFormatter(){
        override fun getFormattedValue(value: Float): String {
            return sqLite.getDate(value.toInt())
        }
    }
    private class ValueFormater: IndexAxisValueFormatter(){
        override fun getFormattedValue(value: Float): String {
            return value.toInt().toString()
        }
    }
    private suspend fun jsoup(){
        loadNewBooks()
    }
    suspend fun updateRecycler(){
        withContext(Main){
            binding.textNewBooks.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()

        }
    }

    private suspend fun loadNewBooks(){
        Log.i("App", "loadNewBooks")
        withContext(IO){
            try {
                //val url = "https://1000kitap.com/kitaplar?s=yeni-cikanlar"
                val calendar = Calendar.getInstance()
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                val url = "https://1000kitap.com/kitaplar?s=yeni-cikanlar&zaman=${month+1}-$year"
                val doc = Jsoup.connect(url).get()

                val elements = doc.select("li.kitap.butonlu")
                for (i in 0 until elements.size) {
                    val bookDetailUrl = elements.select("div.resim")
                        .select("a")
                        .eq(i)
                        .attr("href")
                    val imgUrl = elements.select("div.resim")
                        .select("a")
                        .eq(i)
                        .select("img")
                        .attr("src")
                    val title = elements.select("div.baslik")
                        .select("a")
                        .eq(i)
                        .text()
                    val subTitle = elements.select("div[class=bilgi]")
                        .eq(i)
                        .select("a")
                        .eq(0)
                        .text()
                    Log.i("App", imgUrl)
                    Log.i("App", title)
                    listNetworkData.add(NetworkInfo(imgUrl,title,subTitle,bookDetailUrl))
                }
            }catch (e: IOException){
                e.printStackTrace()
            }
            if(listNetworkData.size >0){
                updateRecycler()
            }

        }
    }

}
