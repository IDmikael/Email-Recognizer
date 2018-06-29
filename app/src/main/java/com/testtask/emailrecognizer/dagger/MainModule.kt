package com.testtask.emailrecognizer.dagger

import android.os.Handler
import android.os.Looper
import com.testtask.emailrecognizer.MainModel
import com.testtask.emailrecognizer.MainPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule(private val view: MainModel.View) {

    @Provides
    @Singleton
    fun provideHomeView(): MainModel.View = view

    @Provides
    @Singleton
    fun provideMainHandler(): Handler = Handler(Looper.getMainLooper())

    @Provides
    @Singleton
    fun provideHomePresenter(view: MainModel.View): MainPresenter = MainPresenter(view)
}