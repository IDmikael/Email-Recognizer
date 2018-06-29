package com.testtask.emailrecognizer.dagger

import com.testtask.emailrecognizer.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(MainModule::class)])
interface MainComponent {
    fun inject(activity: MainActivity)
}