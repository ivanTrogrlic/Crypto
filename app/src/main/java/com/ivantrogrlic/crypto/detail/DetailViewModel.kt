package com.ivantrogrlic.crypto.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
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
 * Created by ivantrogrlic on 01/03/2018.
 */

@Singleton
class DetailViewModel @Inject constructor(private val id: String,
                                          private val rxSharedPreferences: RxSharedPreferences,
                                          private val cryptoRepository: CryptoRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val commands = PublishSubject.create<Command>()
    val detailState = MutableLiveData<DetailState>()

    init {
        compositeDisposable.add(commandExecutor())
    }

    override fun onCleared() = compositeDisposable.clear()

    fun refreshCurrency() = commands.onNext(Command.FetchData)

    private fun commandExecutor(): Disposable {
        return commands
                .flatMap { execute(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ detailState.value = it })
    }

    private fun execute(command: Command): Observable<DetailState> =
            when (command) {
                Command.FetchData -> fetchCryptoCurrency()
            }

    private fun fetchCryptoCurrency() =
            Observable.combineLatest(limit(), currency(), toLimitCurrencyPair())
                    .flatMap { cryptoRepository.fetchCryptoCurrency(id, it.first, it.second) }
                    .map { DetailState.Currency(it) as DetailState }
                    .onErrorReturn { DetailState.Error(it.message) }

    private fun limit(): Observable<Int> = rxSharedPreferences.limit().asObservable()

    private fun currency(): Observable<String> = rxSharedPreferences.currency().asObservable()

    private fun toLimitCurrencyPair(): BiFunction<Int, String, Pair<Int, String>> =
            BiFunction { limit, currency -> Pair(limit, currency) }

}

sealed class DetailState {
    data class Currency(val currency: Crypto?) : DetailState()
    data class Error(val error: String?) : DetailState()
}

sealed class Command {
    object FetchData : Command()
}

class DetailViewModelFactory(private val id: String,
                             private val rxSharedPreferences: RxSharedPreferences,
                             private val repository: CryptoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(id, rxSharedPreferences, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
