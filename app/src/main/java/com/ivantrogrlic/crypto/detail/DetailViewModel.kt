package com.ivantrogrlic.crypto.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.ivantrogrlic.crypto.db.CryptoDao
import com.ivantrogrlic.crypto.model.Crypto
import com.ivantrogrlic.crypto.model.Currency
import com.ivantrogrlic.crypto.utils.currency
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by ivantrogrlic on 01/03/2018.
 */

@Singleton
class DetailViewModel @Inject constructor(private val id: String,
                                          private val rxSharedPreferences: RxSharedPreferences,
                                          private val cryptoDao: CryptoDao) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val commands = PublishSubject.create<Command>()
    val detailState = MutableLiveData<State>()

    init {
        compositeDisposable.add(model())
    }

    override fun onCleared() = compositeDisposable.clear()

    fun loadCurrency() = commands.onNext(Command.LoadData)

    private fun model(): Disposable {
        val fetchDataReducer = commands
                .ofType(Command.LoadData::class.java)
                .flatMap { currency() }
                .switchMap { load(it) }

        return fetchDataReducer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ detailState.value = it })
    }

    private fun load(currency: String) =
            cryptoDao.loadAById(id)
                    .toObservable()
                    .map { State.ShowCurrency(it, Currency.valueOf(currency)) as State }
                    .onErrorReturn { State.ShowError(it.message) }
                    .subscribeOn(Schedulers.io())

    private fun currency(): Observable<String> = rxSharedPreferences.currency().asObservable()

}

sealed class State {
    data class ShowCurrency(val crypto: Crypto, val currency: Currency) : State()
    data class ShowError(val error: String?) : State()
}

sealed class Command {
    object LoadData : Command()
}

class DetailViewModelFactory(private val id: String,
                             private val rxSharedPreferences: RxSharedPreferences,
                             private val cryptoDao: CryptoDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(id, rxSharedPreferences, cryptoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
