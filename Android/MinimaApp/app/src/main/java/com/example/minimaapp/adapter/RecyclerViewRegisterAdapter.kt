package com.example.minimaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minimaapp.Count
import com.example.minimaapp.R

class RecyclerViewRegisterAdapter: RecyclerView.Adapter<RecyclerViewRegisterAdapter.viewHolder>(){

    private var data = emptyList<Count>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_register,parent,false)
        return viewHolder(inflater)
    }

    override fun getItemCount(): Int {
       return data.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.count.text = data[position].count.toString()
        holder.time.text = data[position].time.toString()
        holder.date.text = data[position].date

    }

    fun setData(count: List<Count>){
        this.data = count
        notifyDataSetChanged()
    }


    inner class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val count: TextView = itemView.findViewById<TextView>(R.id.recycler_count)
        val time: TextView = itemView.findViewById<TextView>(R.id.recycler_time)
        val date: TextView = itemView.findViewById<TextView>(R.id.recycler_date)
    }
}