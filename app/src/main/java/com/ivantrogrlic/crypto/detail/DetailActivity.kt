package com.ivantrogrlic.crypto.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import com.ivantrogrlic.crypto.R
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Created by ivantrogrlic on 01/03/2018.
 */

class DetailActivity : DaggerAppCompatActivity() {

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

        val detailViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(DetailViewModel::class.java)

        detailViewModel.refreshCurrency()
        detailViewModel.detailState
                .observe(this, Observer<DetailState> {
                    makeText(this, it.toString(), LENGTH_SHORT).show()
                })
    }

}
