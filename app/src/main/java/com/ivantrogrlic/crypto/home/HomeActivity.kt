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
import com.miguelcatalan.materialsearchview.MaterialSearchView
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

        homeViewModel.homeState.observe(this, Observer { render(it) })
        homeViewModel.refreshCurrency()

        swipeRefresh.setOnRefreshListener({ homeViewModel.refreshCurrency() })
        observeSearchChanges(homeViewModel)
    }

    override fun onBackPressed() {
        if (searchView.isSearchOpen) searchView.closeSearch()
        else super.onBackPressed()
    }

    override fun navigateTo(screen: Screen) =
            when (screen) {
                is DetailScreen -> startActivity(DetailActivity.create(this, screen.id))
                is SettingsScreen -> startActivity(SettingsActivity.create(this))
            }

    override fun goBack() = onBackPressed()

    private fun render(it: State?) {
        it?.let {
            if (adapter == null) {
                adapter = CryptoAdapter(it.currency,
                        it.currencies,
                        { navigateTo(DetailScreen(it)) })
                cryptoCurrencyList.adapter = adapter
            } else {
                adapter!!.setCryptoCurrencies(it.currencies)
                adapter!!.setCurrency(it.currency)
            }

            if (it.canLoadIn > 0) {
                showToast(getString(R.string.cannot_reload, it.canLoadIn))
            }

            swipeRefresh.isRefreshing = it.isLoading

            it.error?.let { showToast(R.string.failed_loading_error_message) }
        }
    }

    private fun setupToolbar() {
        toolbar.inflateMenu(R.menu.home_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> {
                    navigateTo(SettingsScreen())
                    return@setOnMenuItemClickListener true
                }
                R.id.action_search -> {
                    searchView.showSearch()
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
    }

    private fun observeSearchChanges(homeViewModel: HomeViewModel) {
        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                homeViewModel.setFilter(newText)
                return false
            }
        })
    }

}

sealed class Screen {
    class DetailScreen(val id: String) : Screen()
    class SettingsScreen : Screen()
}

interface Navigator {
    fun navigateTo(screen: Screen)
    fun goBack()
}
