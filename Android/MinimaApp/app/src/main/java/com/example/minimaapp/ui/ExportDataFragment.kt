package com.example.minimaapp.ui


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider

import com.example.minimaapp.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder

/**
 * A simple [Fragment] subclass.
 */
class ExportDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        /*val data = StringBuilder()
        data.append("Time,Distance")
        data.append("\n1,1")
        try{

            //Saving file
            val out: FileOutputStream = requireContext().openFileOutput("count_data.csv", Context.MODE_PRIVATE)
            out.write((data.toString()).toByteArray())
            out.close()



            //Exporting
            val fileLocation: File = File(requireContext().filesDir,"count_data.csv")
            fileLocation.createNewFile()
            val path = FileProvider.getUriForFile(requireContext(),"com.example.minimaapp.fileprovider",fileLocation)

            //Save Data
            val createDocIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/csv"
                putExtra(Intent.EXTRA_TITLE,"count_data.csv")
               *//* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    putExtra(DocumentsContract.EXTRA_INITIAL_URI, path)
                }*//*
            }
            startActivityForResult(createDocIntent,1)

            //Send Data
            *//*val fileIntent = Intent(Intent.ACTION_SEND)
            fileIntent.apply {
                setType("text/csv")
                putExtra(Intent.EXTRA_SUBJECT,"Data")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_STREAM,path)
                startActivity(Intent.createChooser(fileIntent,"Send mail"))
            }*//*
        }catch (e: Exception){

        }
*/


        return inflater.inflate(R.layout.fragment_export_data, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val veri = StringBuilder()
        veri.append("Time,Distance")
        veri.append("\n1,1")
        if(resultCode == Activity.RESULT_OK){
            if(requestCode==1){
                try{
                    val contentResolver = requireContext().applicationContext.contentResolver
                    data?.data?.let {
                        contentResolver.openFileDescriptor(it,"w")?.use {
                            FileOutputStream(it.fileDescriptor).use {
                                it.write(veri.toString().toByteArray())
                            }
                        }
                    }
                }catch (e: FileNotFoundException){

                }catch (e: IOException){

                }

            }
        }
    }


}
