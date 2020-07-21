package com.example.minimaapp

data class Book(
    val imgUrl: String,
    val title: String,
    val author: String,
    val bookDetailUrl: String,
    var bookDetail: String = "",
    var isExtanded: Boolean = false
)