package com.example.minimaapp.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.minimaapp.Book
import com.example.minimaapp.Count

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