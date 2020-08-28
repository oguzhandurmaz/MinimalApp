package com.example.minimaapp.di

import com.example.minimaapp.MainActivity
import com.example.minimaapp.di.main.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule{

    @MainScope
    @ContributesAndroidInjector(modules =
    [
        MainFragmentBuildersModule::class,
        MainViewModelsModule::class,
        MainModule::class

    ])
    abstract fun contributeMainActivity(): MainActivity
}