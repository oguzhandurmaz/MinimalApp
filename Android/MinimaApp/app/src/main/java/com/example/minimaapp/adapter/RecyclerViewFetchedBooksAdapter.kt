package com.example.minimaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.transition.ViewTransition
import com.example.minimaapp.data.Book
import com.example.minimaapp.IRecyclerOnClickListener
import com.example.minimaapp.databinding.RecyclerviewFetchedbooksBinding

class RecyclerViewFetchedBooksAdapter(
    private val callback: IRecyclerOnClickListener
) :
    ListAdapter<Book, RecyclerViewFetchedBooksAdapter.RecyclerViewHolder>(BookDiffCallback()) {

    /* var books = listOf<Book>()
         set(value) {
             field = value
             notifyDataSetChanged()
         }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        /*val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_fetchedbooks, parent, false)
        return RecyclerViewHolder(view)*/

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerviewFetchedbooksBinding.inflate(
            layoutInflater, parent, false
        )
        return RecyclerViewHolder(binding)
    }

    /* override fun getItemCount(): Int {
         return books.size
     }*/

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        holder.binding.fetchedBooks = getItem(position)
        holder.binding.executePendingBindings()

        ViewCompat.setTransitionName(holder.binding.recyclerBookImage,getItem(position).title)


        /*  holder.binding.recyclerBookAuthor.text = books[position].author
          holder.binding.recyclerBookTitle.text = books[position].title
  */
        /*



          holder.binding.favorite.setOnCheckedChangeListener { _, isChecked ->
              //TODO Add Favorite List
          }



          Glide.with(holder.itemView.context)
              .load(books[position].imgUrl)
              .placeholder(R.drawable.ic_image)
              .into(holder.binding.recyclerBookImage)*/

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

    /* fun setData(books: List<Book>) {
         this.books = books
         notifyDataSetChanged()
     }*/
    /*val title: TextView = binding.findViewById(R.id.recycler_book_title)
       val author: TextView = binding.findViewById(R.id.recycler_book_author)
       val image: ImageView = binding.findViewById(R.id.recycler_book_image)
       val favorite: CheckBox = binding.findViewById(R.id.favorite)*/


    class RecyclerViewHolder(val binding: RecyclerviewFetchedbooksBinding) :
        RecyclerView.ViewHolder(binding.root)


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