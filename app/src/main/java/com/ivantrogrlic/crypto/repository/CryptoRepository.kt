package com.ivantrogrlic.crypto.repository

import com.ivantrogrlic.crypto.model.Crypto
import com.ivantrogrlic.crypto.rest.CryptoWebService
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

/**
 * Created by ivantrogrlic on 27/02/2018.
 */

@Singleton
class CryptoRepository(private val cryptoWebService: CryptoWebService) {

    fun fetchCryptoCurrencies(limit: Int, convert: String): Observable<List<Crypto>> =
            cryptoWebService.getCryptoCurrencies(limit, convert)
                    .subscribeOn(Schedulers.io())

    fun fetchCryptoCurrency(id: String, limit: Int, convert: String): Observable<Crypto> =
            cryptoWebService.getCrypto(id, limit, convert)
                    .map { it[0] } // TODO
                    .subscribeOn(Schedulers.io())

}
