package com.ivantrogrlic.crypto.home

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ivantrogrlic.crypto.R
import dagger.android.AndroidInjection
import javax.inject.Inject


class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_home)

        val homeViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(HomeViewModel::class.java)

        homeViewModel.refreshCryptoCurrencies()
    }

}
