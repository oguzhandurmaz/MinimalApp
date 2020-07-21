package com.example.minimaapp.adapter

import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.minimaapp.Book
import com.example.minimaapp.IRecyclerOnClickListener
import com.example.minimaapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class RecyclerViewFetchedBooksAdapter(
    private val callback: IRecyclerOnClickListener
) :
    RecyclerView.Adapter<RecyclerViewFetchedBooksAdapter.RecyclerViewHolder>() {

    private var books = emptyList<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_fetchedbooks, parent, false)
        return RecyclerViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.author.text = books[position].author
        holder.title.text = books[position].title


        holder.favorite.setOnCheckedChangeListener { _, isChecked ->
            //TODO Add Favorite List
        }



        Glide.with(holder.itemView.context)
            .load(books[position].imgUrl)
            .placeholder(R.drawable.ic_image)
            .into(holder.image)

        holder.itemView.setOnClickListener{
            callback.onClickListener(position,books[position].imgUrl,books[position].title,books[position].author,books[position].bookDetailUrl)
        }


    }

    fun setData(books: List<Book>) {
        this.books = books
        notifyDataSetChanged()
    }


    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.recycler_book_title)
        val author: TextView = itemView.findViewById(R.id.recycler_book_author)
        val image: ImageView = itemView.findViewById(R.id.recycler_book_image)
        val favorite: CheckBox = itemView.findViewById(R.id.favorite)
    }
}