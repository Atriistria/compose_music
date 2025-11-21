package com.atri.composemusic.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: T)

    @Delete
    suspend fun delete(entity: T): Int
}