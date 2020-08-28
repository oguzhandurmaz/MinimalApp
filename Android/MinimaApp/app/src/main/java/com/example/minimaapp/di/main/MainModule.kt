package com.example.minimaapp.di.main

import com.example.minimaapp.IRecyclerOnClickListener
import com.example.minimaapp.MainActivity
import com.example.minimaapp.adapter.RecyclerViewFetchedBooksAdapter
import com.example.minimaapp.adapter.RecyclerViewRegisterAdapter
import com.example.minimaapp.adapter.ViewPagerGamesAdapter
import com.example.minimaapp.ui.FetchedBooksFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
object MainModule {

    @MainScope
    @Provides
    fun provideRegisterAdapter(): RecyclerViewRegisterAdapter {
        return RecyclerViewRegisterAdapter()
    }

    @MainScope
    @Provides
    fun provideViewPagerGamesAdapter() = ViewPagerGamesAdapter()


}