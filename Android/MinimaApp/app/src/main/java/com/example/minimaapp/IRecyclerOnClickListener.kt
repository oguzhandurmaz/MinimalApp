package com.example.minimaapp

interface IRecyclerOnClickListener {

    fun onClickListener(position: Int,url: String,title: String,author: String,detailUrl: String)
    fun onDeleteListener(position: Int){}
}