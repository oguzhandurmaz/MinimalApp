package com.example.minimaapp.repo

import com.example.minimaapp.data.Book
import com.example.minimaapp.data.dao.CountDao
import com.example.minimaapp.data.table.Count
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*

class CountRepository(private val countDao: CountDao) {

    val countSeven = countDao.getLastSevenData()

    /*suspend fun getDataSeven(): List<Count>{
        return countDao.getLastSevenData()
    }
*/


    suspend fun insert(count: Count) {
        countDao.insert(count)
    }

    suspend fun getAllData(): List<Count> {
        return countDao.getAllData()
    }

    suspend fun getBooks(): List<Book> {
        return withContext(IO) {
            try {
                val bookList = mutableListOf<Book>()
                val calendar = Calendar.getInstance()
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                val url = "https://1000kitap.com/kitaplar?s=yeni-cikanlar&zaman=${month + 1}-$year"
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

                    //Get Book Detail
                    /* val docDetail = Jsoup.connect(bookDetailUrl).get()
                     val elements = docDetail.select("div.oge.metin")
                         .eq(0)
                     val bookDetail = elements.text()*/
                    bookList.add(
                        Book(
                            imgUrl,
                            title,
                            subTitle,
                            bookDetailUrl
                        )
                    )
                }

                return@withContext bookList
            } catch (e: IOException) {
                return@withContext emptyList<Book>()
            }

        }
    }
}