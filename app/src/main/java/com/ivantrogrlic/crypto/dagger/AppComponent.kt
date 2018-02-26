package com.ivantrogrlic.crypto.dagger

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.ivantrogrlic.crypto.CryptoApplication
import com.ivantrogrlic.crypto.rest.RestComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by ivantrogrlic on 26/02/2018.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class))
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

    fun netComponentBuilder(): RestComponent.Builder

    fun sharedPreferences(): SharedPreferences

}