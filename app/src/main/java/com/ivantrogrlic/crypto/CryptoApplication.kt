package com.ivantrogrlic.crypto

import com.ivantrogrlic.crypto.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


/**
 * Created by ivantrogrlic on 26/02/2018.
 */

class CryptoApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent
                .builder()
                .application(this)
                .build()
    }

}
