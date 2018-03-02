package com.ivantrogrlic.crypto.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import com.ivantrogrlic.crypto.R
import com.ivantrogrlic.crypto.detail.DetailActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject


class HomeActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val homeViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(HomeViewModel::class.java)

        homeViewModel.refreshCurrency()
        homeViewModel.homeState
                .observe(this, Observer {
                    makeText(this, it.toString(), LENGTH_SHORT).show()
                })

        start_button.setOnClickListener {
            val intent = DetailActivity.create(application, "bitcoin") // TODO pass id
            startActivity(intent)
        }
    }

}
