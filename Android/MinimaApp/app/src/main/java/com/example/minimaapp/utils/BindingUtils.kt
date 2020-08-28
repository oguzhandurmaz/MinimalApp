package com.example.minimaapp.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.minimaapp.data.Book
import com.example.minimaapp.data.table.BookTable
import com.example.minimaapp.data.table.Count
import com.example.minimaapp.R
import javax.inject.Inject

@BindingAdapter("recyclerBookTitle")
fun TextView.setTitle(item: Book){
    text = item.title
}

@BindingAdapter("recyclerBookImage")
fun ImageView.setImage(item: Book){


    Glide.with(this)
        .load(item.imgUrl)
        .into(this)
}

@BindingAdapter("recyclerBookAuthor")
fun TextView.setAuthor(item: Book){
    text = item.author
}

@BindingAdapter("date")
fun TextView.setDate(item: Count){
    text = item.date
}
@BindingAdapter("count")
fun TextView.setCount(item: Count){
    text = item.count.toString()
}
@BindingAdapter("time")
fun TextView.setTime(item: Count){
    text = item.time.toString()
}
@BindingAdapter("recyclerFavBookTitle")
fun TextView.setTitle(item: BookTable){
    text = item.title
}
@BindingAdapter("recyclerFavBookImage")
fun ImageView.setImage(item: BookTable){
    Glide.with(this)
        .load(item.url)
        .into(this)
}

@BindingAdapter("recyclerFavBookAuthor")
fun TextView.setAuthor(item: BookTable){
    text = item.author
}

//Detail Fragment Image
@BindingAdapter("detailImageLoad")
fun ImageView.setImage(url: String){
    Glide.with(this)
        .load(url)
        //.placeholder(R.drawable.ic_image)
        .into(this)
}