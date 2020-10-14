package com.sserra.mylists.framework.datasource.cache.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sserra.mylists.framework.datasource.cache.model.MyListCacheEntity

@Dao
interface MyListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(myListCacheEntity: MyListCacheEntity) : Long

    @Query("SELECT * FROM lists")
    fun getAllLists() : LiveData<List<MyListCacheEntity>>
}