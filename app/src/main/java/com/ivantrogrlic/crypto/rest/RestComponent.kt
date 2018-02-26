package com.ivantrogrlic.crypto.rest

import com.ivantrogrlic.crypto.dagger.PerServer
import dagger.Subcomponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by ivantrogrlic on 26/02/2018.
 */

@PerServer
@Subcomponent(modules = arrayOf(RestModule::class))
interface RestComponent {

    fun retrofit(): Retrofit
    fun okHttp(): OkHttpClient
    fun cryptoWebService(): CryptoWebService

    @Subcomponent.Builder
    interface Builder {
        fun restModule(restModule: RestModule): Builder
        fun build(): RestComponent
    }

}
