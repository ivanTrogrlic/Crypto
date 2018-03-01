package com.ivantrogrlic.crypto.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.ivantrogrlic.crypto.model.Crypto
import com.ivantrogrlic.crypto.repository.CryptoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by ivantrogrlic on 27/02/2018.
 */

@Singleton
class HomeViewModel @Inject constructor(private val cryptoRepository: CryptoRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val homeState = MutableLiveData<HomeState>()

    override fun onCleared() = compositeDisposable.clear()

    fun refreshCryptoCurrencies() {
        cryptoRepository.fetchCryptoCurrencies(100, "EUR") // TODO user will provide these
                .map { HomeState.Currencies(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ state, error -> reduceState(state, error) })
    }

    private fun reduceState(state: HomeState.Currencies?, error: Throwable?) {
        error?.let { homeState.value = HomeState.Error(it.message) }
        state?.let { homeState.value = it }
    }

}

sealed class HomeState {
    data class Currencies(val currencies: List<Crypto>) : HomeState()
    data class Error(val error: String?) : HomeState()
}
