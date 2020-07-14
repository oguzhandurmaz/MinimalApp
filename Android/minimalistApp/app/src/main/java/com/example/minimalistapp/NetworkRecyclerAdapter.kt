package com.example.minimalistapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class NetworkRecyclerAdapter(private val data: List<NetworkInfo>) :
    RecyclerView.Adapter<NetworkRecyclerAdapter.viewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NetworkRecyclerAdapter.viewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.layout_network_recycler,parent,false)

        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: NetworkRecyclerAdapter.viewHolder, position: Int) {
        val title = "${position+1}.${data[position].title}"
        holder.txtView.text = title
        holder.txtSubView.text = data[position].subTitle
        Glide.with(holder.itemView)
            .load(data[position].imgUrl)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,DetailActivity::class.java)
            intent.putExtra("imgUrl",data[position].imgUrl)
            intent.putExtra("title",data[position].title)
            intent.putExtra("writer",data[position].subTitle)
            intent.putExtra("detailUrl",data[position].detailUrl)
            holder.itemView.context.startActivity(intent)
        }
    }

    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.imageRecycler)
        val txtView: TextView = itemView.findViewById(R.id.titleRecycler)
        val txtSubView: TextView = itemView.findViewById(R.id.subtitleRecycler)
    }
}