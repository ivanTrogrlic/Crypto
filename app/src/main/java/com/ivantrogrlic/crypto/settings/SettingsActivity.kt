package com.ivantrogrlic.crypto.settings

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.EditText
import com.ivantrogrlic.crypto.R
import com.ivantrogrlic.crypto.detail.DetailActivity
import com.ivantrogrlic.crypto.model.Currency
import com.ivantrogrlic.crypto.utils.currency
import com.ivantrogrlic.crypto.utils.limit
import com.ivantrogrlic.crypto.utils.saveCurrency
import com.ivantrogrlic.crypto.utils.saveLimit
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject


/**
 * Created by ivantrogrlic on 07/03/2018.
 */

class SettingsActivity : DaggerAppCompatActivity() {

    companion object {
        fun create(context: Context): Intent =
                Intent(context, DetailActivity::class.java)
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        currency.text = sharedPreferences.currency()
        limit.text = sharedPreferences.limit().toString()

        currencyContainer.setOnClickListener { showCurrencyDialog() }
        limitContainer.setOnClickListener { showLimitDialog() }
    }

    private fun showCurrencyDialog() {
        val currencies = Currency.values().map { it.name }.toTypedArray()
        val currentCurrency = Currency.valueOf(sharedPreferences.currency()).name
        val currencyIndex = Currency.values().indexOfFirst { it.name == currentCurrency }

        AlertDialog.Builder(this)
                .setTitle(R.string.set_currency_title)
                .setSingleChoiceItems(currencies, currencyIndex, null)
                .setPositiveButton(R.string.set, { dialog, _ ->
                    handleCurrencySelected(dialog, currencies)
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show()
    }

    private fun showLimitDialog() {
        val dialogView = layoutInflater.inflate(R.layout.limit_dialog, null)
        val limitInput = dialogView.findViewById<EditText>(R.id.limit_edit)
        limitInput.setText(sharedPreferences.limit().toString())

        AlertDialog.Builder(this)
                .setTitle(R.string.set_limit_title)
                .setView(dialogView)
                .setPositiveButton(R.string.set, { dialog, _ ->
                    handleLimitSelected(limitInput, dialog)
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show()
    }

    private fun handleCurrencySelected(dialog: DialogInterface, currencies: Array<String>) {
        val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
        val selectedCurrency = currencies[selectedPosition]

        sharedPreferences.saveCurrency(selectedCurrency)
        currency.text = selectedCurrency

        dialog.dismiss()
    }

    private fun handleLimitSelected(limitInput: EditText, dialog: DialogInterface) {
        val limitValue = limitInput.text.toString()
        sharedPreferences.saveLimit(limitValue.toInt())
        limit.text = limitValue

        dialog.dismiss()
    }

}
