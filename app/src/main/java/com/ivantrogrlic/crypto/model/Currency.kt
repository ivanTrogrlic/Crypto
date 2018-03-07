package com.ivantrogrlic.crypto.model

import android.content.Context
import com.ivantrogrlic.crypto.R

/**
 * Created by ivantrogrlic on 07/03/2018.
 */

enum class Currency {
    EUR {
        override fun getSymbol(context: Context): String =
                context.getString(R.string.eur_symbol)
    },
    USD {
        override fun getSymbol(context: Context): String =
                context.getString(R.string.usd_symbol)
    },
    CNY {
        override fun getSymbol(context: Context): String =
                context.getString(R.string.cny_symbol)
    };

    abstract fun getSymbol(context: Context): String
}
