package com.ivantrogrlic.crypto.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.ivantrogrlic.crypto.model.Crypto
import com.ivantrogrlic.crypto.repository.CryptoRepository
import com.ivantrogrlic.crypto.utils.currency
import com.ivantrogrlic.crypto.utils.limit
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by ivantrogrlic on 27/02/2018.
 */

@Singleton
class HomeViewModel @Inject constructor(private val cryptoRepository: CryptoRepository,
                                        private val rxSharedPreferences: RxSharedPreferences) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val commands = PublishSubject.create<Command>()
    val homeState = MutableLiveData<State>()

    init {
        compositeDisposable.add(model())
    }

    override fun onCleared() = compositeDisposable.clear()

    fun refreshCurrency() = commands.onNext(Command.FetchData)

    private fun model(): Disposable {
        val limitCurrency = Observable.combineLatest(limit(), currency(), toLimitCurrencyPair())

        val fetchDataReducer = commands
                .ofType(Command.FetchData::class.java)
                .flatMap { limitCurrency }
                .switchMap { fetchCryptoCurrencies(it.first, it.second) }

        return fetchDataReducer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ homeState.value = it })
    }

    private fun fetchCryptoCurrencies(limit: Int, currency: String) =
            cryptoRepository.fetchCryptoCurrencies(limit, currency)
                    .map { State.ShowCurrencies(it) as State }
                    .onErrorReturn { State.ShowError(it.message) }

    private fun limit(): Observable<Int> = rxSharedPreferences.limit().asObservable()

    private fun currency(): Observable<String> = rxSharedPreferences.currency().asObservable()

    private fun toLimitCurrencyPair(): BiFunction<Int, String, Pair<Int, String>> =
            BiFunction { limit, currency -> Pair(limit, currency) }

}

sealed class State {
    data class ShowCurrencies(val currencies: List<Crypto>) : State()
    data class ShowError(val error: String?) : State()
}

sealed class Command {
    object FetchData : Command()
}