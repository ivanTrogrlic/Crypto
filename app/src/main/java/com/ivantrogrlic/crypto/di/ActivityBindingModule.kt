package com.ivantrogrlic.crypto.di

import com.ivantrogrlic.crypto.detail.DetailActivity
import com.ivantrogrlic.crypto.detail.DetailModule
import com.ivantrogrlic.crypto.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by ivantrogrlic on 28/02/2018.
 */

@Module
abstract class ActivityBindingModule {

    @PerActivity
    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(DetailModule::class))
    abstract fun contributeDetailActivity(): DetailActivity

}
