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

fun RxSharedPreferences.limit() = getInteger(KEY_LIMIT, DEFAULT_LIMIT)
fun SharedPreferences.limit() = getInteger(KEY_LIMIT, DEFAULT_LIMIT)
fun SharedPreferences.saveLimit(limit: Int) = edit().putInt(KEY_LIMIT, limit).apply()

fun RxSharedPreferences.currency() = getString(KEY_CURRENCY, DEFAULT_CURRENCY)
fun SharedPreferences.currency() = getString(KEY_CURRENCY, DEFAULT_CURRENCY)
fun SharedPreferences.saveCurrency(currency: String) = edit().putString(KEY_CURRENCY, currency).apply()

fun Context.showToast(@StringRes message: Int) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
