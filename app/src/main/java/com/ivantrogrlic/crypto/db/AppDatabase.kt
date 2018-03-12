package com.ivantrogrlic.crypto.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.ivantrogrlic.crypto.model.Crypto


/**
 * Created by ivantrogrlic on 09/03/2018.
 */

@Database(entities = [(Crypto::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cryptoDao(): CryptoDao
}
