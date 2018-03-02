package com.ivantrogrlic.crypto.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.ivantrogrlic.crypto.detail.Command
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
    val homeState = MutableLiveData<HomeState>()

    init {
        compositeDisposable.add(commandExecutor())
    }

    override fun onCleared() = compositeDisposable.clear()

    fun refreshCurrency() = commands.onNext(Command.FetchData)

    private fun commandExecutor(): Disposable {
        return commands
                .flatMap { execute(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ homeState.value = it })
    }

    private fun execute(command: Command): Observable<HomeState> =
            when (command) {
                Command.FetchData -> fetchCryptoCurrencies()
            }

    private fun fetchCryptoCurrencies() =
            Observable.combineLatest(limit(), currency(), toLimitCurrencyPair())
                    .flatMap { cryptoRepository.fetchCryptoCurrencies(it.first, it.second) }
                    .map { HomeState.Currencies(it) as HomeState }
                    .onErrorReturn { HomeState.Error(it.message) }

    private fun limit(): Observable<Int> = rxSharedPreferences.limit().asObservable()

    private fun currency(): Observable<String> = rxSharedPreferences.currency().asObservable()

    private fun toLimitCurrencyPair(): BiFunction<Int, String, Pair<Int, String>> =
            BiFunction { limit, currency -> Pair(limit, currency) }

}

sealed class HomeState {
    data class Currencies(val currencies: List<Crypto>) : HomeState()
    data class Error(val error: String?) : HomeState()
}
