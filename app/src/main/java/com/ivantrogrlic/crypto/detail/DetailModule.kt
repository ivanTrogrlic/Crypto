package com.ivantrogrlic.crypto.detail

import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.ivantrogrlic.crypto.db.CryptoDao
import com.ivantrogrlic.crypto.di.CryptoKey
import com.ivantrogrlic.crypto.di.PerActivity
import dagger.Module
import dagger.Provides

/**
 * Created by ivantrogrlic on 02/03/2018.
 */

@Module
class DetailModule {

    @Provides
    @PerActivity
    @CryptoKey
    fun provideId(activity: DetailActivity): String {
        return activity.intent.getStringExtra(DetailActivity.KEY_ID)
    }

    @Provides
    @PerActivity
    fun bindDetailViewModelFactory(@CryptoKey id: String,
                                   rxSharedPreferences: RxSharedPreferences,
                                   cryptoDao: CryptoDao): DetailViewModelFactory {
        return DetailViewModelFactory(id, rxSharedPreferences, cryptoDao)
    }

}
