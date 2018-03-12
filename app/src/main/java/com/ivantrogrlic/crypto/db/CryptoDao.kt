package com.ivantrogrlic.crypto.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.ivantrogrlic.crypto.model.Crypto
import io.reactivex.Single

/**
 * Created by ivantrogrlic on 09/03/2018.
 */

@Dao
interface CryptoDao {
    @Query("SELECT * FROM crypto")
    fun all(): Single<List<Crypto>>

    @Query("SELECT * FROM crypto WHERE id = :id")
    fun loadAById(id: String): Single<Crypto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cryptos: List<Crypto>)
}
