package com.ivantrogrlic.crypto.model

import java.sql.Timestamp

/**
 * Created by ivantrogrlic on 27/02/2018.
 */

data class Crypto(val id: String, val name: String, val symbol: String, val rank: Long,
                  val priceUsd: Double, val priceBtc: String, val dayVolumeUsd: Double,
                  val marketCapUsd: Double, val availableSupply: Double, val totalSupply: Double,
                  val percentChangeHour: Double, val percentChangeDay: Double,
                  val percentChangeWeek: Double, val lastUpdatedTmstp: Timestamp)
