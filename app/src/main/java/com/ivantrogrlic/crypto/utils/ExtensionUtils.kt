package com.ivantrogrlic.crypto.utils

import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.StringRes
import android.widget.Toast
import com.f2prateek.rx.preferences2.RxSharedPreferences
import java.lang.Integer.getInteger

/**
 * Created by ivantrogrlic on 02/03/2018.
 */

const val DEFAULT_LIMIT = 100
const val DEFAULT_CURRENCY = "USD"
const val KEY_LIMIT = "key_crypto_currency_limit"
const val KEY_CURRENCY = "key_crypto_currency"
const val KEY_LAST_LOADED = "key_last_loaded"

fun RxSharedPreferences.limit() = getInteger(KEY_LIMIT, DEFAULT_LIMIT)
fun RxSharedPreferences.currency() = getString(KEY_CURRENCY, DEFAULT_CURRENCY)
fun RxSharedPreferences.lastLoaded(currency: String) = getLong(KEY_LAST_LOADED + currency)

fun SharedPreferences.limit() = getInteger(KEY_LIMIT, DEFAULT_LIMIT)
fun SharedPreferences.saveLimit(limit: Int) = edit().putInt(KEY_LIMIT, limit).apply()
fun SharedPreferences.currency() = getString(KEY_CURRENCY, DEFAULT_CURRENCY)
fun SharedPreferences.lastLoaded(currency: String) = getLong(KEY_LAST_LOADED + currency, 0)
fun SharedPreferences.saveCurrency(currency: String) = edit().putString(KEY_CURRENCY, currency).apply()
fun SharedPreferences.saveLastLoaded(currency: String, timestamp: Long) = edit().putLong(KEY_LAST_LOADED + currency, timestamp).apply()


fun Context.showToast(@StringRes message: Int) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
fun Context.showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
