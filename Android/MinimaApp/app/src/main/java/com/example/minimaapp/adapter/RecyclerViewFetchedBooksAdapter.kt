package com.example.minimaapp.adapter

import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minimaapp.IRecyclerOnClickListener
import com.example.minimaapp.data.Book
import com.example.minimaapp.databinding.RecyclerviewFetchedbooksBinding
import com.example.minimaapp.databinding.RecyclerviewFetchedbooksHeaderBinding
import com.example.minimaapp.utils.Constants
import javax.inject.Inject

class RecyclerViewFetchedBooksAdapter (
    private val callback: IRecyclerOnClickListener
) :
    ListAdapter<Book, RecyclerView.ViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType){
            Constants.FETCHED_BOOKS_HEADER -> RecyclerViewHolderHeader(
                RecyclerviewFetchedbooksHeaderBinding.inflate(layoutInflater,parent,false))
            else -> RecyclerViewHolder(RecyclerviewFetchedbooksBinding.inflate(
                layoutInflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(getItemViewType(position)){
           Constants.FETCHED_BOOKS_HEADER -> {
               val headerHolder= (holder as RecyclerViewHolderHeader)

               holder.itemView.setOnClickListener {
                   val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FETCHED_BOOK_URL))

                   val activities: List<ResolveInfo> = holder.itemView.context.packageManager.queryIntentActivities(intent,0)
                   val isIntentSafe = activities.isNotEmpty()
                   if(isIntentSafe){
                       holder.itemView.context.startActivity(intent)
                   }
               }
           }
            else -> {
                val defaultHolder = holder as RecyclerViewHolder
                holder.binding.fetchedBooks = getItem(position)
                holder.binding.executePendingBindings()

                ViewCompat.setTransitionName(holder.binding.recyclerBookImage,getItem(position).title)

                holder.itemView.setOnClickListener {
                    callback.onClickListener(
                        position,
                        getItem(position).imgUrl,
                        getItem(position).title,
                        getItem(position).author,
                        getItem(position).bookDetailUrl,
                        holder.binding.recyclerBookImage
                    )
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> Constants.FETCHED_BOOKS_HEADER
            else -> 1
        }
    }

    class RecyclerViewHolder(val binding: RecyclerviewFetchedbooksBinding) :
        RecyclerView.ViewHolder(binding.root)

    class RecyclerViewHolderHeader(val binding: RecyclerviewFetchedbooksHeaderBinding): RecyclerView.ViewHolder(binding.root)


    //DiffUtil
    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }

    }
}