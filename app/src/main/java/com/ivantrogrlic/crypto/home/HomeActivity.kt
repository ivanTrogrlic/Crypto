package com.ivantrogrlic.crypto.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import com.ivantrogrlic.crypto.R
import com.ivantrogrlic.crypto.detail.DetailActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject


class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_home)

        val homeViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(HomeViewModel::class.java)

        homeViewModel.refreshCryptoCurrencies()
        homeViewModel.homeState
                .observe(this, Observer {
                    makeText(this, it.toString(), LENGTH_SHORT).show()
                })

        start_button.onClick {
            val intent = DetailActivity.create(application, "bitcoin") // TODO pass id
            startActivity(intent)
        }
    }

}
