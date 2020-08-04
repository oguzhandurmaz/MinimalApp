package com.example.minimaapp.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.minimaapp.viewmodel.FavoriteBooksViewModel
import com.example.minimaapp.IRecyclerOnClickListener
import com.example.minimaapp.adapter.RecyclerViewFavoriteBooksAdapter
import com.example.minimaapp.databinding.FragmentFavoriteBooksBinding

/**
 * A simple [Fragment] subclass.
 */
class FavoriteBooksFragment : Fragment(), IRecyclerOnClickListener {
    override fun onClickListener(
        position: Int,
        url: String,
        title: String,
        author: String,
        detailUrl: String
    ) {
        val action =
            FavoriteBooksFragmentDirections.actionFavoriteBooksFragmentToBookDetailFragment(
                url,
                title,
                author,
                detailUrl
            )
        findNavController().navigate(action)
    }

    override fun onDeleteListener(position: Int) {
        viewModel.delete(position)
    }

    private lateinit var binding: FragmentFavoriteBooksBinding

    private lateinit var viewModel: FavoriteBooksViewModel
    private lateinit var viewModelFactory: FavoriteBooksViewModel.FavoriteBooksViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoriteBooksBinding.inflate(inflater)

        viewModelFactory =
            FavoriteBooksViewModel.FavoriteBooksViewModelFactory(
                requireActivity().application
            )
        viewModel = ViewModelProvider(this,viewModelFactory).get(FavoriteBooksViewModel::class.java)

        val adapter = RecyclerViewFavoriteBooksAdapter(this)
        binding.recyclerFavBooks.adapter = adapter

        viewModel.favBooks.observe(viewLifecycleOwner, Observer {
            it?.let {   //adapter.submitList(it.toMutableList())
                adapter.setData(it)
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }


}
