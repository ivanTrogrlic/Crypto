package com.ivantrogrlic.crypto.rest

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by ivantrogrlic on 26/02/2018.
 */

interface CryptoWebService {

    @GET("ticker/")
    fun cryptoCurrencies(@Query("limit") limit: Int,
                         @Query("convert") convert: String): Single<String>

    @GET("ticker/{id}/")
    fun crypto(@Query("id") id: String): Single<String>

}
