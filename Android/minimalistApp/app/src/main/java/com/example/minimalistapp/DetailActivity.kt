package com.example.minimalistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imageUrl = intent.getStringExtra("imgUrl")
        val title = intent.getStringExtra("title")
        val writer = intent.getStringExtra("writer")
        val detailUrl = intent.getStringExtra("detailUrl")

        Glide.with(this)
            .load(imageUrl)
            .into(imageBook)
        textTitle.text = title
        textWriter.text = writer

        CoroutineScope(IO).launch {
            detailUrl?.let {
                loadFromNet(it)
            }
        }
    }

    private suspend fun loadFromNet(detailUrl: String){
        loadDetailBook(detailUrl)
    }

    private suspend fun updateInterface(detail: String){
        withContext(Main){
            textDetail.text = detail
        }
    }

    private suspend fun loadDetailBook(detailUrl: String){

        withContext(IO){
            val doc = Jsoup.connect(detailUrl).get()

            val elements = doc.select("div.oge.metin")

            val detail = elements.text()

            updateInterface(detail)

        }
    }
}
