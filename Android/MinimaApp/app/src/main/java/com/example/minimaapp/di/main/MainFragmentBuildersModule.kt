package com.example.minimaapp.di.main

import com.example.minimaapp.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeCountFragment(): CountFragment

    @ContributesAndroidInjector
    abstract fun contributeGraphicsFragment(): GraphicsFragment

    @ContributesAndroidInjector
    abstract fun contributeFetchedBooksFragment(): FetchedBooksFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteBooksFragment(): FavoriteBooksFragment

    @ContributesAndroidInjector
    abstract fun contributeBookDetailFragment(): BookDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeGamesFragment(): GamesFragment

    @ContributesAndroidInjector
    abstract fun contributeExportDataFragment(): ExportDataFragment


}