package com.example.minimaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.minimaapp.data.table.Count
import com.example.minimaapp.databinding.RecyclerviewRegisterBinding

class RecyclerViewRegisterAdapter :
    androidx.recyclerview.widget.ListAdapter<Count, RecyclerViewRegisterAdapter.viewHolder>(
        CountDiffCallback()
    ) {

    //private var data = emptyList<Count>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        /*val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_register, parent, false)
        return viewHolder(inflater)*/

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerviewRegisterBinding.inflate(layoutInflater,parent,false)

        return viewHolder(binding)
    }
/*
    override fun getItemCount(): Int {
        return data.size
    }*/

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.binding.count = getItem(position)
        holder.binding.executePendingBindings()


       /* binding.recyclerCount.text = getItem(position).count.toString()
        binding.recyclerTime.text = getItem(position).time.toString()
        binding.recyclerDate.text = getItem(position).date*/

    }

   /* fun setData(count: List<Count>) {
        this.data = count
        notifyDataSetChanged()
    }
*/

    class viewHolder(val binding: RecyclerviewRegisterBinding) : RecyclerView.ViewHolder(binding.root) {
      /*  val count: TextView = binding.findViewById<TextView>(R.id.recycler_count)
        val time: TextView = binding.findViewById<TextView>(R.id.recycler_time)
        val date: TextView = binding.findViewById<TextView>(R.id.recycler_date)*/

    }

    class CountDiffCallback : DiffUtil.ItemCallback<Count>() {
        override fun areItemsTheSame(oldItem: Count, newItem: Count): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Count, newItem: Count): Boolean {
            return oldItem == newItem
        }


    }
}