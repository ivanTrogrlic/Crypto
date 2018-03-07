package com.ivantrogrlic.crypto.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import com.ivantrogrlic.crypto.R
import com.ivantrogrlic.crypto.settings.SettingsActivity
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
        setupToolbar()

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
                                adapter = CryptoAdapter(it.currency, it.currencies)
                                cryptoCurrencyList.adapter = adapter
                            } else {
                                adapter!!.setCryptoCurrencies(it.currencies)
                                adapter!!.setCurrency(it.currency)
                            }
                        }
                        is State.ShowError -> makeText(this, "FAILED", LENGTH_SHORT).show()
                    }
                })
    }

    private fun setupToolbar() {
        toolbar.inflateMenu(R.menu.home_menu)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.settings_action) {
                // TODO Navigator
                startActivity(Intent(this, SettingsActivity::class.java))
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
    }

}
