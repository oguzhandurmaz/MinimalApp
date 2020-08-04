package com.example.minimaapp.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.minimaapp.ui.BookDetailFragmentArgs
import com.example.minimaapp.viewmodel.BookDetailViewModel
import com.example.minimaapp.viewmodel.BookDetailViewModel.*
import com.example.minimaapp.data.table.BookTable
import com.example.minimaapp.MainActivity
import com.example.minimaapp.databinding.FragmentBookDetailBinding

/**
 * A simple [Fragment] subclass.
 */
class BookDetailFragment : Fragment() {

    private lateinit var binding: FragmentBookDetailBinding


    //ViewModel
    private lateinit var viewModel: BookDetailViewModel
    private lateinit var viewModelFactory: BookDetailViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookDetailBinding.inflate(inflater)

        //ViewModel
        viewModelFactory = BookDetailViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BookDetailViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val args = BookDetailFragmentArgs.fromBundle(requireArguments())
        val imageUrl = args.imageUrl
        val title = args.title
        val author = args.author
        val detailUrl = args.bookDetailUrl

        val actionBar = (activity as MainActivity).supportActionBar
        actionBar?.title = title

        if(detailUrl.startsWith("https://")){
            viewModel.getBookDetail(detailUrl)
        }else{
            viewModel.setBookDetail(detailUrl)
        }

      /*  Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.ic_image)
            .into(binding.imageBook)*/
        binding.imageUrl = imageUrl

        binding.bookTitle.text = title
        binding.bookAuthor.text = author

        viewModel.fetchedBookDetail.observe(viewLifecycleOwner, Observer {
            binding.btnAddFavorite.isEnabled = true
        })


        viewModel.favBooks.observe(viewLifecycleOwner, Observer {
            it.forEach {
               if(it.title == title && it.author == author){
                   binding.btnAddFavorite.isEnabled = false
                   binding.btnAddFavorite.text = "Added Favorites"
                   return@forEach
               }
            }

        })

        viewModel.success.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.btnAddFavorite.isEnabled = false
                binding.btnAddFavorite.text = "Added Favorites"
            }else{
                Toast.makeText(requireContext(),"Couldnt Add to Favorites",Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnAddFavorite.setOnClickListener {
            viewModel.insert(
                BookTable(
                    0,
                    imageUrl,
                    title,
                    author,
                    binding.bookDetail.text.toString()
                )
            )
        }








        return binding.root
    }


}
