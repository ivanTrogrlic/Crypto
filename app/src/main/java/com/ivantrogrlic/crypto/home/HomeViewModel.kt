package com.ivantrogrlic.crypto.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.SharedPreferences
import android.text.TextUtils
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.ivantrogrlic.crypto.db.CryptoDao
import com.ivantrogrlic.crypto.model.Crypto
import com.ivantrogrlic.crypto.model.Currency
import com.ivantrogrlic.crypto.repository.CryptoRepository
import com.ivantrogrlic.crypto.utils.currency
import com.ivantrogrlic.crypto.utils.lastLoaded
import com.ivantrogrlic.crypto.utils.limit
import com.ivantrogrlic.crypto.utils.saveLastLoaded
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by ivantrogrlic on 27/02/2018.
 */

private const val FIVE_MINUTES = 5 * 60 * 1000

@Singleton
class HomeViewModel @Inject constructor(private val cryptoDao: CryptoDao,
                                        private val cryptoRepository: CryptoRepository,
                                        private val sharedPreferences: SharedPreferences,
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
                .replay(1)
                .refCount()

        val localData = cryptoDao.all()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .replay(1)
                .refCount()

        val isLoadingReducer = commands
                .ofType(Command.FetchData::class.java)
                .map { isLoadingReducer(true) }

        val alreadyLoadedReducer = commands
                .ofType(Command.AlreadyLoaded::class.java)
                .map { alreadyLoadedReducer(it.lastLoaded) }

        val currencyReducer = limitCurrency
                .map { currencyReducer(Currency.valueOf(it.second)) }

        val localCurrenciesReducer = localData
                .map { currenciesReducer(it) }

        val fetchDataReducer = commands
                .ofType(Command.FetchData::class.java)
                .throttleLast(100, TimeUnit.MILLISECONDS)
                .flatMap { limitCurrency }
                .switchMap { fetchOrSkip(it) }
                .map { currenciesReducer(it) }
                .onErrorReturn { failedFetchingCurrenciesReducer(it) }

        val filterReducer = commands
                .ofType(Command.Filter::class.java)
                .map { it.filter }
                .switchMap { filter -> localData.map { filterReducer(it, filter) } }

        val reducers = listOf(currencyReducer,
                localCurrenciesReducer,
                fetchDataReducer,
                filterReducer,
                isLoadingReducer,
                alreadyLoadedReducer)

        return Observable.merge(reducers)
                .scan(State.initialState(), { state, reducer -> reducer(state) })
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ homeState.value = it })
    }

    private fun fetchOrSkip(it: Pair<Int, String>): Observable<List<Crypto>>? {
        val lastLoadedTmstp = sharedPreferences.lastLoaded(it.second)
        return if (Date().time > lastLoadedTmstp + FIVE_MINUTES) {
            fetchCryptoCurrencies(it.first, it.second)
        } else {
            commands.onNext(Command.AlreadyLoaded(lastLoadedTmstp))
            Observable.empty()
        }
    }

    private fun fetchCryptoCurrencies(limit: Int, currency: String) =
            cryptoRepository.fetchCryptoCurrencies(limit, currency)
                    .doOnNext({ cryptoDao.insertAll(it) })
                    .doOnNext({ sharedPreferences.saveLastLoaded(currency, Date().time) })

    private fun filterByName(filter: String, it: Crypto) =
            if (!TextUtils.isEmpty(filter)) it.name.startsWith(filter, true) else true

    private fun limit(): Observable<Int> = rxSharedPreferences.limit().asObservable()

    private fun currency(): Observable<String> = rxSharedPreferences.currency().asObservable()

    private fun toLimitCurrencyPair(): BiFunction<Int, String, Pair<Int, String>> =
            BiFunction { limit, currency -> Pair(limit, currency) }

    private fun filterReducer(currencies: List<Crypto>, filter: String): StateReducer = { state ->
        val filteredCurrencies = currencies.filter { filterByName(filter, it) }
        state.copy(currencies = filteredCurrencies, error = null, canLoadIn = 0)
    }

    private fun currenciesReducer(currencies: List<Crypto>): StateReducer = { state ->
        state.copy(currencies = currencies, error = null, isLoading = false, canLoadIn = 0)
    }

    private fun currencyReducer(currency: Currency): StateReducer = { state ->
        state.copy(currency = currency, canLoadIn = 0)
    }

    private fun failedFetchingCurrenciesReducer(error: Throwable): StateReducer = { state ->
        state.copy(error = error.message, isLoading = false, canLoadIn = 0)
    }

    private fun alreadyLoadedReducer(lastLoaded: Long): StateReducer = { state ->
        val canLoadIn = (FIVE_MINUTES - (Date().time - lastLoaded)) / 1000
        state.copy(canLoadIn = canLoadIn, isLoading = false)
    }

    private fun isLoadingReducer(isLoading: Boolean): StateReducer = { state ->
        state.copy(isLoading = isLoading, canLoadIn = 0)
    }

}

typealias StateReducer = (State) -> State

data class State(val currency: Currency,
                 val currencies: List<Crypto>,
                 val canLoadIn: Long,
                 val isLoading: Boolean,
                 val error: String? = null) {
    companion object {
        fun initialState() = State(Currency.USD, emptyList(), 0, true)
    }
}

sealed class Command {
    object FetchData : Command()
    data class AlreadyLoaded(val lastLoaded: Long) : Command()
    data class Filter(val filter: String) : Command()
}
