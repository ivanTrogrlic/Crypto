package com.ivantrogrlic.crypto.dagger

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.ivantrogrlic.crypto.rest.RestModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by ivantrogrlic on 26/02/2018.
 */

@Module(includes = arrayOf(RestModule::class, ViewModelModule::class))
class AppModule {

    @Singleton
    @Provides
    @ApplicationContext
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun sharedPreferencesName(): String = "CryptoPrefs"

    @Provides
    @Singleton
    fun sharedPreferences(@ApplicationContext context: Context,
                          sharedPreferencesName: String): SharedPreferences =
            context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

}
