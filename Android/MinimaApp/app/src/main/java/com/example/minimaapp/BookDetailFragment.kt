package com.example.minimaapp


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.minimaapp.BookDetailViewModel.*
import com.example.minimaapp.databinding.FragmentBookDetailBinding
import org.jsoup.Jsoup

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
        viewModel = ViewModelProvider(this,viewModelFactory).get(BookDetailViewModel::class.java)

        val args = BookDetailFragmentArgs.fromBundle(requireArguments())
        val imageUrl = args.imageUrl
        val title = args.title
        val author = args.author
        val detailUrl = args.bookDetailUrl

        val actionBar = (activity as MainActivity).supportActionBar
        actionBar?.title = title

        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.ic_image)
            .into(binding.imageBook)

        binding.bookTitle.text = title
        binding.bookAuthor.text = author

        viewModel.getBookDetail(detailUrl)

        viewModel.fetchedBookDetail.observe(viewLifecycleOwner, Observer {
            binding.bookDetail.text = it
        })







        return binding.root
    }


}
