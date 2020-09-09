package com.example.minimaapp.ui


import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.minimaapp.MainActivity
import com.example.minimaapp.R
import com.example.minimaapp.adapter.ViewPagerGamesAdapter
import com.example.minimaapp.databinding.FragmentGamesBinding
import com.example.minimaapp.utils.Constants.CHESS
import com.example.minimaapp.utils.Constants.DRAUGHTS
import com.example.minimaapp.utils.Constants.MANGALA
import com.example.minimaapp.utils.Constants.RUBIK
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_games.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class GamesFragment : DaggerFragment() {

    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!

    private var actionBar: androidx.appcompat.app.ActionBar? = null

    private var mediator: TabLayoutMediator? = null

    @Inject
    lateinit var adapter: ViewPagerGamesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGamesBinding.inflate(inflater)

        setHasOptionsMenu(true)

        actionBar = (activity as MainActivity).supportActionBar
        actionBar?.setShowHideAnimationEnabled(false)
        actionBar?.hide()

        binding.toolbarGames.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        //val adapter = ViewPagerGamesAdapter()
        binding.viewPager.adapter = adapter


         mediator = TabLayoutMediator(binding.tabLayout,binding.viewPager){ tab, position ->
            when(position){
                CHESS -> {
                    tab.apply {
                        text = getString(R.string.chess)
                        icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_chess)
                    }
                }
                RUBIK -> {
                    tab.apply {
                        text = getString(R.string.rubik)
                        icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_rubik_cube)
                    }
                }
                MANGALA -> {
                    tab.apply {
                        text = getString(R.string.mangala)
                        icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_mangala)
                    }
                }
                DRAUGHTS -> {
                    tab.apply {
                        text = getString(R.string.draught)
                        icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_dama)
                    }
                }
            }
        }
        mediator?.attach()
        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onStop() {
        super.onStop()
        actionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediator?.detach()
        mediator = null
        actionBar = null
        binding.viewPager.adapter = null
        _binding = null
    }


}
