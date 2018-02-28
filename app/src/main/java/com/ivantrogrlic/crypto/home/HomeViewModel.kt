package com.ivantrogrlic.crypto.home

import android.arch.lifecycle.ViewModel
import com.ivantrogrlic.crypto.model.Crypto
import com.ivantrogrlic.crypto.repository.CryptoRepository
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by ivantrogrlic on 27/02/2018.
 */

@Singleton
class HomeViewModel @Inject constructor(private val cryptoRepository: CryptoRepository) : ViewModel() {

    private var homeState: BehaviorSubject<HomeState> =
            BehaviorSubject.createDefault(HomeState.Currencies(emptyList()))

    fun observeHomeState(): Observable<HomeState> = homeState.hide()

    fun refreshCryptoCurrencies() {
        cryptoRepository.fetchCryptoCurrencies(100, "EUR") // TODO user will provide these
                .map { HomeState.Currencies(it) }
                .subscribe({ state, error -> reduceState(state, error) })
    }

    private fun reduceState(state: HomeState.Currencies?, error: Throwable?) {
        error?.let { homeState.onNext(HomeState.Error(it.message)) }
        state?.let { homeState.onNext(it) }
    }

}

sealed class HomeState {
    data class Currencies(val currencies: List<Crypto>) : HomeState()
    data class Error(val error: String?) : HomeState()
}
