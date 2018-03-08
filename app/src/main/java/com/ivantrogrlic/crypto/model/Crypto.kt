package com.ivantrogrlic.crypto.model

import com.google.gson.annotations.SerializedName

/**
 * Created by ivantrogrlic on 27/02/2018.
 */

data class Crypto(val id: String,
                  val name: String,
                  val symbol: String,
                  val rank: Long,
                  @SerializedName("price_usd")
                  val priceUsd: String,
                  @SerializedName("price_eur")
                  val priceEur: String?,
                  @SerializedName("price_cny")
                  val priceCny: String?,
                  @SerializedName("price_btc")
                  val priceBtc: String,
                  @SerializedName("24h_volume_usd")
                  val dayVolumeUsd: String,
                  @SerializedName("24h_volume_eur")
                  val dayVolumeEur: String?,
                  @SerializedName("24h_volume_cny")
                  val dayVolumeCny: String?,
                  @SerializedName("market_cap_usd")
                  val marketCapUsd: String,
                  @SerializedName("market_cap_eur")
                  val marketCapEur: String?,
                  @SerializedName("market_cap_cny")
                  val marketCapCny: String?,
                  @SerializedName("available_supply")
                  val availableSupply: String,
                  @SerializedName("total_supply")
                  val totalSupply: String,
                  @SerializedName("percent_change_1h")
                  val percentChangeHour: String,
                  @SerializedName("percent_change_24h")
                  val percentChangeDay: String,
                  @SerializedName("percent_change_7d")
                  val percentChangeWeek: String,
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
