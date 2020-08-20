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
import com.example.minimaapp.utils.Constants.RUBIK
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_games.*

/**
 * A simple [Fragment] subclass.
 */
class GamesFragment : Fragment() {

    private lateinit var binding: FragmentGamesBinding

    private var actionBar: androidx.appcompat.app.ActionBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGamesBinding.inflate(inflater)

        setHasOptionsMenu(true)

        actionBar = (activity as MainActivity).supportActionBar
        actionBar?.setShowHideAnimationEnabled(false)
        actionBar?.hide()

        binding.toolbarGames.title = "Games"

        binding.toolbarGames.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        val adapter = ViewPagerGamesAdapter()
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout,binding.viewPager){ tab, position ->
            when(position){
                CHESS -> {
                    tab.apply {
                        text = "Chess"
                        icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_chess)
                    }
                }
                RUBIK -> {
                    tab.apply {
                        text = "Rubik"
                        icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_rubik_cube)
                    }
                }

            }
        }.attach()
        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onStop() {
        super.onStop()
        actionBar?.show()
    }


}
