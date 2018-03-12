package com.ivantrogrlic.crypto.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.ivantrogrlic.crypto.db.AppDatabase
import com.ivantrogrlic.crypto.db.CryptoDao
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

    @Provides
    @Singleton
    fun rxSharedPreferences(sharedPreferences: SharedPreferences): RxSharedPreferences =
            RxSharedPreferences.create(sharedPreferences)

    @Provides
    @Singleton
    fun appDatabase(@ApplicationContext context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "crypto_db").build()

    @Provides
    @Singleton
    fun cryptoDao(appDatabase: AppDatabase): CryptoDao = appDatabase.cryptoDao()

}
