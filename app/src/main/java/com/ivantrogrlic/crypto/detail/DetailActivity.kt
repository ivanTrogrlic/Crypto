package com.ivantrogrlic.crypto.detail

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ivantrogrlic.crypto.CryptoApplication
import com.ivantrogrlic.crypto.R
import com.ivantrogrlic.crypto.di.PerActivity
import com.ivantrogrlic.crypto.repository.CryptoRepository
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Inject

/**
 * Created by ivantrogrlic on 01/03/2018.
 */

class DetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_ID = "key_crypto_id"
        fun create(context: Context, id: String): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(KEY_ID, id)
            return intent
        }
    }

    @Inject
    lateinit var viewModelFactory: DetailViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val id = intent.getStringExtra(KEY_ID)
        CryptoApplication.appComponent.inject(DetailModule(id)).inject(this)

        val detailViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(DetailViewModel::class.java)

        detailViewModel.fetchCryptoCurrency()
    }

}

@PerActivity
@Subcomponent(modules = arrayOf(DetailModule::class))
interface DetailComponent {
    fun inject(activity: DetailActivity)
}

@Module
class DetailModule(private val id: String) {

    @Provides
    fun bindDetailViewModelFactory(repository: CryptoRepository): DetailViewModelFactory {
        return DetailViewModelFactory(id, repository)
    }

}
