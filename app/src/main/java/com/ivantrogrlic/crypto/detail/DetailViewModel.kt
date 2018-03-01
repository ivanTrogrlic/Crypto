package com.ivantrogrlic.crypto.detail

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ivantrogrlic.crypto.model.Crypto
import com.ivantrogrlic.crypto.repository.CryptoRepository
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by ivantrogrlic on 01/03/2018.
 */

@Singleton
class DetailViewModel @Inject constructor(private val id: String,
                                          private val cryptoRepository: CryptoRepository) : ViewModel() {

    private var detailState: BehaviorSubject<DetailState> =
            BehaviorSubject.createDefault(DetailState.Currency(null))

    override fun onCleared() = detailState.onComplete()

    fun observeDetailState(): Observable<DetailState> = detailState.hide()

    fun fetchCryptoCurrency() {
        cryptoRepository.fetchCryptoCurrency(id, 100, "EUR") // TODO user will provide these
                .map { DetailState.Currency(it) }
                .subscribe({ state, error -> reduceState(state, error) })
    }

    private fun reduceState(state: DetailState.Currency?, error: Throwable?) {
        error?.let { detailState.onNext(DetailState.Error(it.message)) }
        state?.let { detailState.onNext(it) }
    }

}

sealed class DetailState {
    data class Currency(val currency: Crypto?) : DetailState()
    data class Error(val error: String?) : DetailState()
}

class DetailViewModelFactory(private val id: String,
                             private val repository: CryptoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(id, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
