package com.ivantrogrlic.crypto.rest

import com.ivantrogrlic.crypto.model.Crypto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by ivantrogrlic on 26/02/2018.
 */

interface CryptoWebService {

    @GET("ticker/")
    fun getCryptoCurrencies(@Query("limit") limit: Int,
                            @Query("convert") convert: String): Single<List<Crypto>>

    @GET("ticker/{id}/")
    fun getCrypto(@Query("id") id: String): Single<Crypto>

}
