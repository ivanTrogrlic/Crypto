package com.ivantrogrlic.crypto.di

import com.ivantrogrlic.crypto.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by ivantrogrlic on 28/02/2018.
 */

@Module
abstract class HomeActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity

}
