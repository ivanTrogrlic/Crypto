package com.ivantrogrlic.crypto.rest

import com.ivantrogrlic.crypto.BuildConfig
import com.ivantrogrlic.crypto.home.CryptoRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by ivantrogrlic on 26/02/2018.
 */

@Module
class RestModule {

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .baseUrl(BuildConfig.CRYPTO_BASE_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()

    @Provides
    @Singleton
    fun cryptoWebService(retrofit: Retrofit): CryptoWebService =
            retrofit.create(CryptoWebService::class.java)

    @Provides
    @Singleton
    fun cryptoRepository(cryptoWebService: CryptoWebService): CryptoRepository =
            CryptoRepository(cryptoWebService)

}
