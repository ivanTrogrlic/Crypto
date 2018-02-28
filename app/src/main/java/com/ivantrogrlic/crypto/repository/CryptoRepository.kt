package com.ivantrogrlic.crypto.repository

import com.ivantrogrlic.crypto.model.Crypto
import com.ivantrogrlic.crypto.rest.CryptoWebService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

/**
 * Created by ivantrogrlic on 27/02/2018.
 */

@Singleton
class CryptoRepository(private val cryptoWebService: CryptoWebService) {

    fun fetchCryptoCurrencies(limit: Int, convert: String): Single<List<Crypto>> =
            cryptoWebService.getCryptoCurrencies(limit, convert)
                    .subscribeOn(Schedulers.io())

}
