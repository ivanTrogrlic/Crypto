package com.ivantrogrlic.crypto.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import com.ivantrogrlic.crypto.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject


class HomeActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var adapter: CryptoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val homeViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(HomeViewModel::class.java)

        cryptoCurrencyList.layoutManager = LinearLayoutManager(this)
        cryptoCurrencyList.addItemDecoration(DividerItemDecoration(this, VERTICAL))

        homeViewModel.refreshCurrency()
        homeViewModel.homeState
                .observe(this, Observer {
                    when (it) {
                        is State.ShowCurrencies -> {
                            if (adapter == null) {
                                adapter = CryptoAdapter(it.currencies.toMutableList())
                                cryptoCurrencyList.adapter = CryptoAdapter(it.currencies.toMutableList())
                            } else {
                                adapter!!.setCryptoCurrencies(it.currencies)
                            }
                        }
                        is State.ShowError -> makeText(this, "FAILED", LENGTH_SHORT).show()
                    }
                })
    }

}
