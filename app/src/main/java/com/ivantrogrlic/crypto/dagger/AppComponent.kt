package com.ivantrogrlic.crypto.dagger

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.ivantrogrlic.crypto.CryptoApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by ivantrogrlic on 26/02/2018.
 */

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class,
        AppModule::class,
        HomeActivityModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: CryptoApplication)

    @ApplicationContext
    fun context(): Context

    fun sharedPreferences(): SharedPreferences

}