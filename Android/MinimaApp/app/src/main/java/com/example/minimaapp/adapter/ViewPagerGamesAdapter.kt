package com.example.minimaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.minimaapp.R
import com.example.minimaapp.utils.Constants.CHESS
import com.example.minimaapp.utils.Constants.RUBIK

class ViewPagerGamesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CHESS -> {
                ChessViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_chess,
                        parent,
                        false
                    )
                )
            }
            RUBIK -> {
                RubikViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_rubik,
                        parent,
                        false
                    )
                )
            }
            else -> RubikViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.layout_rubik,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> CHESS
            1 -> RUBIK
            else -> RUBIK
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }


    class ChessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    class RubikViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}