package com.ivantrogrlic.crypto

import android.app.Application
import com.ivantrogrlic.crypto.dagger.AppComponent
import com.ivantrogrlic.crypto.dagger.DaggerAppComponent
import com.ivantrogrlic.crypto.rest.RestComponent

/**
 * Created by ivantrogrlic on 26/02/2018.
 */

class CryptoApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
        var netComponent: RestComponent? = null
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build()
        appComponent.inject(this)
    }

    fun component() = appComponent
    fun netComponent() = netComponent

}