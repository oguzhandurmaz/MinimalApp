package com.example.minimaapp.di

import com.example.minimaapp.CommonReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadcastReceiverModule {

    @ContributesAndroidInjector
    abstract fun contributeCommonReceiver(): CommonReceiver
}