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
    val detailState = MutableLiveData<State>()

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
                .switchMap { fetchCryptoCurrency(it.first, it.second) }

        // In case more reducers/commands are required just create new reducer, and then return the
        // merge result of the state.

        return fetchDataReducer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ detailState.value = it })
    }

    private fun fetchCryptoCurrency(limit: Int, currency: String) =
            cryptoRepository.fetchCryptoCurrency(id, limit, currency)
                    .map { State.ShowCurrency(it) as State }
                    .onErrorReturn { State.ShowError(it.message) }

    private fun limit(): Observable<Int> = rxSharedPreferences.limit().asObservable()

    private fun currency(): Observable<String> = rxSharedPreferences.currency().asObservable()

    private fun toLimitCurrencyPair(): BiFunction<Int, String, Pair<Int, String>> =
            BiFunction { limit, currency -> Pair(limit, currency) }

}

sealed class State {
    data class ShowCurrency(val currency: Crypto?) : State()
    data class ShowError(val error: String?) : State()
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
