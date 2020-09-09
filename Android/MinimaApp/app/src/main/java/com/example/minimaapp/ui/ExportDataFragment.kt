package com.example.minimaapp.ui


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.minimaapp.R
import com.example.minimaapp.data.table.Count
import com.example.minimaapp.viewmodel.ExportDataViewModel
import com.example.minimaapp.viewmodel.ViewModelProviderFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import com.example.minimaapp.databinding.FragmentExportDataBinding
import com.example.minimaapp.utils.Constants.SAVE_DATA
import dagger.android.support.DaggerFragment

/**
 * A simple [Fragment] subclass.
 */
class ExportDataFragment : DaggerFragment() {

    private var _binding: FragmentExportDataBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    private lateinit var viewModel: ExportDataViewModel

    private val exportData = StringBuilder()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExportDataBinding.inflate(inflater)

        viewModel = ViewModelProvider(this,viewModelFactory).get(ExportDataViewModel::class.java)


        viewModel.getAllData()

        Toast.makeText(requireContext(),"Veri hazırlanıyor...",Toast.LENGTH_SHORT).show()
        viewModel.allData.observe(viewLifecycleOwner, Observer {
            setData(it)
        })

        binding.btnCsv.setOnClickListener {
            saveData("csv")
        }
        binding.btnXlxs.setOnClickListener {
            saveData("xls")
        }
        binding.btnSend.setOnClickListener {
            sendData()
        }


        return binding.root
    }

    private fun setButtonState(state: Boolean){
        binding.apply {
            btnCsv.isEnabled = state
            btnXlxs.isEnabled = state
            btnSend.isEnabled = state
        }
    }

    private fun setData(data: List<Count>){
        CoroutineScope(Main).launch {
            if(data.isEmpty()){
                setButtonState(false)
                binding.exportErrorInfo.isVisible = true
                return@launch
            }
            withContext(Dispatchers.Default) {

                exportData.append("${getString(R.string.count)},${getString(R.string.time)},${getString(R.string.date)}")
                data.forEach {
                    exportData.append("\n${it.count},${it.time},${it.date}")
                }
            }
            Toast.makeText(requireContext(),getString(R.string.data_ready),Toast.LENGTH_SHORT).show()
           setButtonState(true)
        }
    }

    private fun saveData(docFormat: String){
        val createDocIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/$docFormat"
            putExtra(Intent.EXTRA_TITLE,"count_data.$docFormat")
        }
        startActivityForResult(createDocIntent,SAVE_DATA)

    }

    private fun sendData(){

        try{
            val out = requireContext().openFileOutput("count_data.csv",Context.MODE_PRIVATE)
            out.write(exportData.toString().toByteArray())
            out.close()

            val file = File(requireContext().filesDir,"count_data.csv")
            val path = FileProvider.getUriForFile(requireContext(),"com.example.minimaapp.fileprovider",file)

            Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_SUBJECT,"Exported Data")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_STREAM,path)
                startActivity(Intent.createChooser(this,getString(R.string.choose_app)))
            }
        }catch (e: Exception){

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode== SAVE_DATA){
                try{
                    val contentResolver = requireContext().applicationContext.contentResolver
                    data?.data?.let {uri ->
                        contentResolver.openFileDescriptor(uri,"w")?.use { fileDescriptor ->
                            FileOutputStream(fileDescriptor.fileDescriptor).use {
                                it.write(exportData.toString().toByteArray())
                            }
                        }
                    }
                    Toast.makeText(requireContext(),getString(R.string.data_saved),Toast.LENGTH_SHORT).show()
                }catch (e: FileNotFoundException){
                    Toast.makeText(requireContext(),getString(R.string.error_file_save),Toast.LENGTH_SHORT).show()
                }catch (e: IOException){
                    Toast.makeText(requireContext(),getString(R.string.error_file_save),Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
