package com.example.minimaapp.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minimaapp.data.table.BookTable
import com.example.minimaapp.IRecyclerOnClickListener
import com.example.minimaapp.databinding.RecyclerviewFavoritebooksBinding

class RecyclerViewFavoriteBooksAdapter(private val callback: IRecyclerOnClickListener) :
    ListAdapter<BookTable, RecyclerViewFavoriteBooksAdapter.viewHolder>(FavBookDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): viewHolder {
        /*val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_favoritebooks, parent, false)
        return viewHolder(view)*/
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerviewFavoritebooksBinding.inflate(
            layoutInflater, parent, false)
        return RecyclerViewFavoriteBooksAdapter.viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.apply {

            binding.favoriteBooks = getItem(position)
            binding.executePendingBindings()
          /*  Glide.with(holder.binding.recyclerFavBookImage)
                .load(getItem(position).url)
                .into(holder.binding.recyclerFavBookImage)

            holder.binding.recyclerFavBookTitle.text = getItem(position).title
            holder.binding.recyclerFavBookAuthor.text = getItem(position).author*/

            holder.binding.recyclerFavBookDelete.setOnClickListener {
                //callback.onDeleteListener(position)
                val alertDialog = AlertDialog.Builder(it.context)
                alertDialog.setTitle("Delete Book")
                alertDialog.setMessage("You are deleting Favorite Book. Are you sure?")
                alertDialog.setPositiveButton("Yes"
                ) { _, _ ->
                   callback.onDeleteListener(position)
                }
                alertDialog.setNegativeButton("No"){
                    dialog, _ ->
                    dialog.dismiss()
                }
                alertDialog.show()
            }

        }

        holder.itemView.setOnClickListener {
            getItem(position).apply {
                callback.onClickListener(position, url, title, author, detail)
            }
        }

    }


    class viewHolder(val binding: RecyclerviewFavoritebooksBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    class FavBookDiffCallback : DiffUtil.ItemCallback<BookTable>() {
        override fun areItemsTheSame(oldItem: BookTable, newItem: BookTable): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BookTable, newItem: BookTable): Boolean {
            return oldItem == newItem
        }

    }
}