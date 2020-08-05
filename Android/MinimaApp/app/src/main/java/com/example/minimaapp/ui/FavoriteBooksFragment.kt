package com.example.minimaapp.ui


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.minimaapp.IRecyclerOnClickListener
import com.example.minimaapp.R
import com.example.minimaapp.adapter.RecyclerViewFavoriteBooksAdapter
import com.example.minimaapp.databinding.FragmentFavoriteBooksBinding
import com.example.minimaapp.viewmodel.FavoriteBooksViewModel
import java.util.*

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


    private lateinit var adapter: RecyclerViewFavoriteBooksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoriteBooksBinding.inflate(inflater)

        viewModelFactory =
            FavoriteBooksViewModel.FavoriteBooksViewModelFactory(
                requireActivity().application
            )
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(FavoriteBooksViewModel::class.java)

        setHasOptionsMenu(true)

        adapter = RecyclerViewFavoriteBooksAdapter(this)
        binding.recyclerFavBooks.adapter = adapter


        viewModel.favBooks.observe(viewLifecycleOwner, Observer {
            it?.let {
                //adapter.submitList(it.toMutableList())
                adapter.setData(it)
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        val inputManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


        inflater.inflate(R.menu.fav_books_menu, menu)

        val searchMenu = menu.findItem(R.id.search_book)
        val searchView = searchMenu.actionView as SearchView

        searchMenu.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.favBooks.value?.let {
                    adapter.setData(it)
                }
                return true
            }

        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                //Filter ViewModel for query
                val filteredList = viewModel.favBooks.value?.filter { bookTable ->
                    bookTable.title.toLowerCase(Locale.getDefault()).contains(query.toString())
                }

                //Set Filter Result to Adapter
                if (filteredList.isNullOrEmpty()) {
                    Toast.makeText(requireContext(),"Eşleşen Kitap Bulunamadı",Toast.LENGTH_SHORT).show()
                } else {
                    adapter.setData(filteredList)
                }

                inputManager.hideSoftInputFromWindow(view?.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

    }


}
