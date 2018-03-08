package com.ivantrogrlic.crypto.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.ivantrogrlic.crypto.R
import com.ivantrogrlic.crypto.model.Crypto
import com.ivantrogrlic.crypto.utils.showToast
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
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
                .observe(this, Observer<State> {
                    it!!.let { render(it) }
                })
    }

    private fun render(state: State) {
        when (state) {
            is State.ShowCurrency -> {
                val crypto = state.crypto
                val currency = state.currency
                toolbar.title = getString(R.string.details_title, crypto.name, crypto.symbol)
                marketCap.text = getString(R.string.market_cap, currency.getSymbol(this), crypto.getMarketCap(currency))
                supply.text = getString(R.string.supply, crypto.totalSupply)
                price.text = getString(R.string.price, currency.getSymbol(this), crypto.getPrice(currency))
                hourChange.text = getString(R.string.change_percent, crypto.percentChangeHour)
                dayChange.text = getString(R.string.change_percent, crypto.percentChangeDay)
                weekChange.text = getString(R.string.change_percent, crypto.percentChangeWeek)

                setupChangeStyle(crypto)
            }
            is State.ShowError -> showToast(R.string.failed_loading_error_message)
        }
    }

    private fun setupChangeStyle(crypto: Crypto) {
        val negativeColor = ContextCompat.getColor(this, R.color.color_negative)
        val positiveColor = ContextCompat.getColor(this, R.color.color_positive)

        val isHourChangeNegative = crypto.percentChangeHour.toDouble() < 0
        val hourArrow = if (isHourChangeNegative) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
        hourChange.setCompoundDrawablesWithIntrinsicBounds(hourArrow, 0, 0, 0)
        hourChange.setTextColor(if (isHourChangeNegative) negativeColor else positiveColor)

        val isDayChangeNegative = crypto.percentChangeDay.toDouble() < 0
        val dayArrow = if (isDayChangeNegative) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
        dayChange.setCompoundDrawablesWithIntrinsicBounds(dayArrow, 0, 0, 0)
        dayChange.setTextColor(if (isDayChangeNegative) negativeColor else positiveColor)

        val isWeekChangeNegative = crypto.percentChangeWeek.toDouble() < 0
        val weekArrow = if (isWeekChangeNegative) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
        weekChange.setCompoundDrawablesWithIntrinsicBounds(weekArrow, 0, 0, 0)
        weekChange.setTextColor(if (isWeekChangeNegative) negativeColor else positiveColor)
    }

}
