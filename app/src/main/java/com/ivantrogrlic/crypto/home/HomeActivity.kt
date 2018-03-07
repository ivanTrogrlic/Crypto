package com.ivantrogrlic.crypto.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout.VERTICAL
import com.ivantrogrlic.crypto.R
import com.ivantrogrlic.crypto.detail.DetailActivity
import com.ivantrogrlic.crypto.home.Screen.DetailScreen
import com.ivantrogrlic.crypto.home.Screen.SettingsScreen
import com.ivantrogrlic.crypto.settings.SettingsActivity
import com.ivantrogrlic.crypto.utils.showToast
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject


class HomeActivity : DaggerAppCompatActivity(), Navigator {

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
                                adapter = CryptoAdapter(it.currency,
                                        it.currencies,
                                        { navigateTo(DetailScreen(it)) })
                                cryptoCurrencyList.adapter = adapter
                            } else {
                                adapter!!.setCryptoCurrencies(it.currencies)
                                adapter!!.setCurrency(it.currency)
                            }
                        }
                        is State.ShowError -> showToast(R.string.failed_loading_error_message)
                    }
                })
    }

    private fun setupToolbar() {
        toolbar.inflateMenu(R.menu.home_menu)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.settings_action) {
                navigateTo(SettingsScreen())
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
    }

    override fun navigateTo(screen: Screen) =
            when (screen) {
                is DetailScreen -> startActivity(DetailActivity.create(this, screen.id))
                is SettingsScreen -> startActivity(SettingsActivity.create(this))
            }

    override fun goBack() = onBackPressed()

}

sealed class Screen {
    class DetailScreen(val id: String) : Screen()
    class SettingsScreen : Screen()
}

interface Navigator {
    fun navigateTo(screen: Screen)
    fun goBack()
}
