package com.ivantrogrlic.crypto.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by ivantrogrlic on 27/02/2018.
 */

@Entity
data class Crypto(@PrimaryKey val id: String,
                  @ColumnInfo(name = "name")
                  val name: String,
                  @ColumnInfo(name = "symbol")
                  val symbol: String,
                  @ColumnInfo(name = "rank")
                  val rank: Long,
                  @ColumnInfo(name = "price_usd")
                  @SerializedName("price_usd")
                  val priceUsd: String,
                  @ColumnInfo(name = "price_eur")
                  @SerializedName("price_eur")
                  val priceEur: String?,
                  @ColumnInfo(name = "price_cny")
                  @SerializedName("price_cny")
                  val priceCny: String?,
                  @ColumnInfo(name = "price_btc")
                  @SerializedName("price_btc")
                  val priceBtc: String,
                  @ColumnInfo(name = "24h_volume_usd")
                  @SerializedName("24h_volume_usd")
                  val dayVolumeUsd: String,
                  @ColumnInfo(name = "24h_volume_eur")
                  @SerializedName("24h_volume_eur")
                  val dayVolumeEur: String?,
                  @ColumnInfo(name = "24h_volume_cny")
                  @SerializedName("24h_volume_cny")
                  val dayVolumeCny: String?,
                  @ColumnInfo(name = "market_cap_usd")
                  @SerializedName("market_cap_usd")
                  val marketCapUsd: String,
                  @ColumnInfo(name = "market_cap_eur")
                  @SerializedName("market_cap_eur")
                  val marketCapEur: String?,
                  @ColumnInfo(name = "market_cap_cny")
                  @SerializedName("market_cap_cny")
                  val marketCapCny: String?,
                  @ColumnInfo(name = "available_supply")
                  @SerializedName("available_supply")
                  val availableSupply: String,
                  @ColumnInfo(name = "total_supply")
                  @SerializedName("total_supply")
                  val totalSupply: String,
                  @ColumnInfo(name = "percent_change_1h")
                  @SerializedName("percent_change_1h")
                  val percentChangeHour: String,
                  @ColumnInfo(name = "percent_change_24h")
                  @SerializedName("percent_change_24h")
                  val percentChangeDay: String,
                  @ColumnInfo(name = "percent_change_7d")
                  @SerializedName("percent_change_7d")
                  val percentChangeWeek: String,
                  @ColumnInfo(name = "last_updated")
                  @SerializedName("last_updated")
                  val lastUpdatedTmstp: Long) {

    fun getPrice(currency: Currency): String =
            when (currency) {
                Currency.USD -> priceUsd
                Currency.EUR -> priceEur!!
                Currency.CNY -> priceCny!!
            }

    fun getDayVolume(currency: Currency): String =
            when (currency) {
                Currency.USD -> dayVolumeUsd
                Currency.EUR -> dayVolumeEur!!
                Currency.CNY -> dayVolumeCny!!
            }

    fun getMarketCap(currency: Currency): String =
            when (currency) {
                Currency.USD -> marketCapUsd
                Currency.EUR -> marketCapEur!!
                Currency.CNY -> marketCapCny!!
            }

}
