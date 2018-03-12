package com.ivantrogrlic.crypto.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.ivantrogrlic.crypto.CryptoApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by ivantrogrlic on 26/02/2018.
 */

@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBindingModule::class))
interface AppComponent : AndroidInjector<CryptoApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    @ApplicationContext
    fun context(): Context

    fun sharedPreferences(): SharedPreferences

}
