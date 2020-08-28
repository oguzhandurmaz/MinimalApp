package com.example.minimaapp.ui


import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.animation.doOnEnd
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.minimaapp.AutoClearedValue
import com.example.minimaapp.AutoClearedValue_LifecycleAdapter
import com.example.minimaapp.ui.BookDetailFragmentArgs
import com.example.minimaapp.viewmodel.BookDetailViewModel
import com.example.minimaapp.viewmodel.BookDetailViewModel.*
import com.example.minimaapp.data.table.BookTable
import com.example.minimaapp.MainActivity
import com.example.minimaapp.databinding.FragmentBookDetailBinding
import com.example.minimaapp.viewmodel.ViewModelProviderFactory
import com.google.android.material.appbar.AppBarLayout
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.math.abs

/**
 * A simple [Fragment] subclass.
 */
class BookDetailFragment : DaggerFragment() {

    private var binding by AutoClearedValue<FragmentBookDetailBinding>()
    //ViewModel
    private lateinit var viewModel: BookDetailViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
   // private lateinit var viewModelFactory: BookDetailViewModelFactory

    private var actionBar: ActionBar? = null


    private var isInDb = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = BookDetailFragmentArgs.fromBundle(requireArguments())

        //binding.imageUrl = args.imageUrl
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        binding = FragmentBookDetailBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        //ViewModel
        //viewModelFactory = BookDetailViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BookDetailViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        actionBar = (activity as MainActivity).supportActionBar
        //actionBar?.title = title

        actionBar?.setShowHideAnimationEnabled(false)
        actionBar?.hide()

        val args = BookDetailFragmentArgs.fromBundle(requireArguments())
        val imageUrl = args.imageUrl
        val title = args.title
        val author = args.author
        val detailUrl = args.bookDetailUrl



        binding.toolbar.title = title
        binding.tempAuthor.text = author

       binding.toolbar.setNavigationOnClickListener {
           findNavController().navigateUp()
       }


        //Collapsing Bar kapanınca Add Favorite göster açılınca gizle.
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            binding.btnAddFavorite.clearAnimation()
            if( ((appBarLayout.totalScrollRange + verticalOffset).toFloat() / appBarLayout.totalScrollRange) == 0.0f)
            {
               val a =  ObjectAnimator.ofFloat(binding.btnAddFavorite,"alpha",0f).apply {
                    duration = 300
                    start()
                }
                ObjectAnimator.ofFloat(binding.btnAddFavorite,"translationY",60f).apply {
                    duration =250
                    start()
                }
                a.doOnEnd { binding.btnAddFavorite.visibility = View.GONE }
            }else{
                val b = ObjectAnimator.ofFloat(binding.btnAddFavorite,"alpha",1f).apply {
                    duration = 300
                    start()
                }
                ObjectAnimator.ofFloat(binding.btnAddFavorite,"translationY",0f).apply {
                    duration = 250
                    start()
                }
                b.doOnEnd { binding.btnAddFavorite.visibility = View.VISIBLE }
            }
        })

        if (detailUrl.startsWith("https://")) {
            viewModel.getBookDetail(detailUrl)
        } else {
            viewModel.setBookDetail(detailUrl)
        }

        binding.imageUrl = imageUrl
        /*viewModel.fetchedBookDetail.observe(viewLifecycleOwner, Observer {
            binding.btnAddFavorite.isEnabled = isInDb
        })*/



        viewModel.favBooks.observe(viewLifecycleOwner, Observer {
            it.forEach {
                if (it.title == title && it.author == author) {
                    isInDb = true
                    binding.btnAddFavorite.isEnabled = false
                    binding.btnAddFavorite.text = "Added Favorites"
                    return@forEach
                }
            }

        })

        viewModel.success.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.btnAddFavorite.isEnabled = false
                binding.btnAddFavorite.text = "Added Favorites"
            } else {
                Toast.makeText(requireContext(), "Couldnt Add to Favorites", Toast.LENGTH_SHORT)
                    .show()
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

    override fun onStop() {
        super.onStop()
        actionBar?.show()
    }


}
