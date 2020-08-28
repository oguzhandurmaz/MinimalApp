package com.example.minimaapp.di.main

import androidx.lifecycle.ViewModel
import com.example.minimaapp.di.ViewModelKey
import com.example.minimaapp.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule{

    @Binds
    @IntoMap
    @ViewModelKey(CountViewModel::class)
    abstract fun bindCountViewModel(viewModel: CountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GraphicsFragmentViewModel::class)
    abstract fun bindGraphicsViewModel(viewModel: GraphicsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteBooksViewModel::class)
    abstract fun bindFavoriteBooksViewModel(viewModel: FavoriteBooksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FetchedBooksViewModel::class)
    abstract fun bindFetchedBooksViewModel(viewModel: FetchedBooksViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(BookDetailViewModel::class)
    abstract fun bindBookDetailViewModel(viewModel: BookDetailViewModel): ViewModel


}