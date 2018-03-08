package com.ivantrogrlic.crypto.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.ivantrogrlic.crypto.model.Crypto
import com.ivantrogrlic.crypto.model.Currency
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
    fun setFilter(filter: String) = commands.onNext(Command.Filter(filter))

    private fun model(): Disposable {
        val limitCurrency = Observable.combineLatest(limit(), currency(), toLimitCurrencyPair())

        val fetchData = commands
                .ofType(Command.FetchData::class.java)
                .flatMap { limitCurrency }
                .switchMap { fetchCryptoCurrencies(it.first, it.second) }
                .replay(1)
                .refCount()

        val fetchDataReducer = fetchData
                .map { fetchCurrenciesReducer(it) }
                .onErrorReturn { failedFetchingCurrenciesReducer(it) }

        val filterReducer = commands
                .ofType(Command.Filter::class.java)
                .map { it.filter }
                .flatMap { filter ->
                    fetchData.map {
                        if (TextUtils.isEmpty(filter)) it
                        else filterCurrencies(filter, it)
                    }
                }.map { filterReducer(it) }

        return Observable.merge(fetchDataReducer, filterReducer)
                .scan(State.initialState(), { state, reducer -> reducer(state) })
                .observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .subscribe({ homeState.value = it })
    }

    private fun fetchCryptoCurrencies(limit: Int, currency: String) =
            cryptoRepository.fetchCryptoCurrencies(limit, currency)


    private fun limit(): Observable<Int> = rxSharedPreferences.limit().asObservable()

    private fun currency(): Observable<String> = rxSharedPreferences.currency().asObservable()

    private fun toLimitCurrencyPair(): BiFunction<Int, String, Pair<Int, String>> =
            BiFunction { limit, currency -> Pair(limit, currency) }

    private fun filterCurrencies(filter: String, currencies: List<Crypto>): List<Crypto> =
            currencies.filter { it.name.startsWith(filter, true) }

    private fun filterReducer(currencies: List<Crypto>): StateReducer = { state ->
        state.copy(currencies = currencies, error = null)
    }

    private fun fetchCurrenciesReducer(currencies: List<Crypto>): StateReducer = { state ->
        state.copy(currencies = currencies, error = null)
    }

    private fun failedFetchingCurrenciesReducer(error: Throwable): StateReducer = { state ->
        state.copy(error = error.message)
    }

}

typealias StateReducer = (State) -> State

data class State(val currency: Currency, val currencies: List<Crypto>, val error: String? = null) {
    companion object {
        fun initialState() = State(Currency.USD, emptyList())
    }
}

sealed class Command {
    object FetchData : Command()
    data class Filter(val filter: String) : Command()
}
