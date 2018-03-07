package com.ivantrogrlic.crypto.di

import com.ivantrogrlic.crypto.detail.DetailActivity
import com.ivantrogrlic.crypto.detail.DetailModule
import com.ivantrogrlic.crypto.home.HomeActivity
import com.ivantrogrlic.crypto.settings.SettingsActivity
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
    @ContributesAndroidInjector
    abstract fun contributeSettingActivity(): SettingsActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(DetailModule::class))
    abstract fun contributeDetailActivity(): DetailActivity

}
