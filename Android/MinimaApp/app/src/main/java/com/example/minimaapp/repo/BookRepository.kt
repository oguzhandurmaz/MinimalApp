package com.example.minimaapp.repo

import com.example.minimaapp.data.Book
import com.example.minimaapp.data.dao.BookDao
import com.example.minimaapp.data.table.BookTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException
import java.lang.Exception
import java.util.*

class BookRepository(private val bookDao: BookDao) {


    //Favorite Books Section
    fun get() = bookDao.getAllBooks()

    suspend fun insert(bookTable: BookTable):Boolean{
        return try{
            bookDao.insert(bookTable)
            true
        }catch (e: Exception){
            false
        }
    }

    suspend fun delete(bookTable: BookTable): Boolean{
        return try{
            bookDao.delete(bookTable)
            true
        }catch (e: Exception){
            false
        }
    }




    //Fetched From 1000kitap.com Latest 25 Books
    suspend fun fetchBooks(): List<Book> {
        return withContext(Dispatchers.IO){
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

    suspend fun getBookDetail(url: String): String{
        return withContext(IO){
            val docDetail = Jsoup.connect(url).get()
            val elements = docDetail.select("div.oge.metin")
                .eq(0)
            return@withContext elements.text()
        }

    }
}