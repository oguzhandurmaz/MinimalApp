package com.example.minimaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.minimaapp.Book
import com.example.minimaapp.R

class RecyclerViewFetchedBooksAdapter :
    RecyclerView.Adapter<RecyclerViewFetchedBooksAdapter.viewHolder>() {

    private var books = emptyList<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_fetchedbooks,parent,false)
        return viewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.author.text = books[position].author
        holder.title.text = books[position].title

        Glide.with(holder.itemView.context)
            .load(books[position].imgUrl)
            .placeholder(R.drawable.ic_image)
            .into(holder.image)
    }

    fun setData(books: List<Book>){
        this.books = books
        notifyDataSetChanged()
    }


    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById<TextView>(R.id.recycler_book_title)
        val author: TextView = itemView.findViewById<TextView>(R.id.recycler_book_author)
        val image: ImageView = itemView.findViewById<ImageView>(R.id.recycler_book_image)
        val favorite: CheckBox = itemView.findViewById(R.id.favorite)
    }
}