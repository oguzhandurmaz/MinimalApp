package com.example.minimaapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.minimaapp.adapter.RecyclerViewFetchedBooksAdapter
import com.example.minimaapp.databinding.FragmentFetchedBooksBinding

/**
 * A simple [Fragment] subclass.
 */
class FetchedBooksFragment : Fragment(), IRecyclerOnClickListener{
    override fun onClickListener(
        position: Int,
        url: String,
        title: String,
        author: String,
        detailUrl: String
    ) {
        val action
                = FetchedBooksFragmentDirections.actionFetchedBooksFragmentToBookDetailFragment(
            url,title,author,detailUrl
        )
        findNavController().navigate(action)
    }

    private lateinit var binding: FragmentFetchedBooksBinding

    //ViewModel
    private lateinit var viewModel: FetchedBooksViewModel
    private lateinit var viewModelFactory: FetchedBooksViewModel.FetchedBooksViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentFetchedBooksBinding.inflate(inflater)

        viewModelFactory = FetchedBooksViewModel.FetchedBooksViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(FetchedBooksViewModel::class.java)


        val adapter = RecyclerViewFetchedBooksAdapter(this)
        binding.recyclerBooks.adapter = adapter

        fetchBooks()

        viewModel.books.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.submitList(it) }
            if(it.isEmpty()){
                Toast.makeText(requireContext(),"Couldn't Load Books",Toast.LENGTH_SHORT).show()
            }
            binding.swipeRefresh.isRefreshing = false
            binding.swipeRefresh.isEnabled = false
        })



        return binding.root
    }

    private fun fetchBooks(){
        binding.swipeRefresh.isEnabled = true
        binding.swipeRefresh.isRefreshing = true
        viewModel.getBooks()
    }


}
