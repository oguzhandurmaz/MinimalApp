package com.example.minimalistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SqlRecyclerAdapter(private val data: List<Register>): RecyclerView.Adapter<SqlRecyclerAdapter.viewHolder>() {

    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtRecCount: TextView = itemView.findViewById(R.id.txtRecyclerCount)
        val txtRecDate: TextView = itemView.findViewById(R.id.txtRecyclerDate)
        val txtRecMaxTime: TextView = itemView.findViewById(R.id.txtRecyclerMaxTime)
        val txtRecAllTime: TextView = itemView.findViewById(R.id.txtRecylerAllTime)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): viewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.layout_sql_register,parent,false)

        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SqlRecyclerAdapter.viewHolder, position: Int) {
        holder.txtRecDate.text = data[position].date
        holder.txtRecCount.text = data[position].count.toString()
        holder.txtRecMaxTime.text = data[position].maxTime.toString()
        holder.txtRecAllTime.text = data[position].allTime.toString()
    }

}