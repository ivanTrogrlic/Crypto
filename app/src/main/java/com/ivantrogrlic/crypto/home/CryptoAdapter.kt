package com.ivantrogrlic.crypto.home

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivantrogrlic.crypto.R
import com.ivantrogrlic.crypto.detail.DetailActivity
import com.ivantrogrlic.crypto.model.Crypto
import kotlinx.android.synthetic.main.crypto_item.view.*


/**
 * Created by ivantrogrlic on 04/03/2018.
 */

class CryptoAdapter(private val cryptoList: MutableList<Crypto>)
    : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.crypto_item, parent, false)

        return CryptoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val crypto = cryptoList[position]
        holder.bind(crypto)
    }

    override fun getItemCount(): Int {
        return cryptoList.size
    }

    fun setCryptoCurrencies(cryptoList: List<Crypto>) {
        this.cryptoList.clear()
        this.cryptoList.addAll(cryptoList)
        notifyDataSetChanged()
    }

    inner class CryptoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(crypto: Crypto) {
            val context = itemView.context
            itemView.setOnClickListener {
                // TODO use navigator
                val intent = DetailActivity.create(context, crypto.id)
                context.startActivity(intent)
            }

            itemView.rank.text = crypto.rank.toString()
            itemView.symbol.text = crypto.symbol
            itemView.name.text = crypto.name
            itemView.price.text = crypto.priceUsd
            itemView.marketCap.text = context.getString(R.string.market_cap, crypto.marketCapUsd)
            itemView.supply.text = context.getString(R.string.supply, crypto.totalSupply)
            itemView.hourChangeValue.text = context.getString(R.string.change_percent, crypto.percentChangeHour)
            itemView.dayChangeValue.text = context.getString(R.string.change_percent, crypto.percentChangeDay)
            itemView.weekChangeValue.text = context.getString(R.string.change_percent, crypto.percentChangeWeek)

            setupChangeStyle(context, itemView, crypto)
        }

        private fun setupChangeStyle(context: Context, itemView: View, crypto: Crypto) {
            val negativeColor = ContextCompat.getColor(context, R.color.color_negative)
            val positiveColor = ContextCompat.getColor(context, R.color.color_positive)

            val isHourChangeNegative = crypto.percentChangeHour.toDouble() < 0
            val hourArrow = if (isHourChangeNegative) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
            itemView.hourChangeValue.setCompoundDrawablesWithIntrinsicBounds(hourArrow, 0, 0, 0)
            itemView.hourChangeValue.setTextColor(if (isHourChangeNegative) negativeColor else positiveColor)

            val isDayChangeNegative = crypto.percentChangeDay.toDouble() < 0
            val dayArrow = if (isDayChangeNegative) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
            itemView.dayChangeValue.setCompoundDrawablesWithIntrinsicBounds(dayArrow, 0, 0, 0)
            itemView.dayChangeValue.setTextColor(if (isDayChangeNegative) negativeColor else positiveColor)

            val isWeekChangeNegative = crypto.percentChangeWeek.toDouble() < 0
            val weekArrow = if (isWeekChangeNegative) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
            itemView.weekChangeValue.setCompoundDrawablesWithIntrinsicBounds(weekArrow, 0, 0, 0)
            itemView.weekChangeValue.setTextColor(if (isWeekChangeNegative) negativeColor else positiveColor)
        }
    }

}
