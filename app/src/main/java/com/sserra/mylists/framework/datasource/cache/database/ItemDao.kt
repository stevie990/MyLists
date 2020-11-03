package com.sserra.mylists.framework.datasource.cache.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sserra.mylists.framework.datasource.cache.model.ItemCacheEntity

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itemCacheEntity: ItemCacheEntity): Long

    @Query("SELECT * FROM items WHERE listId = :listId")
    fun getAllItems(listId: String): LiveData<List<ItemCacheEntity>>

    @Query("UPDATE items SET title = :title, description = :description,  isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateItem(
        id: String,
        title: String,
        description: String,
        isCompleted: Boolean
    ): Int

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun searchItemById(id: String): ItemCacheEntity?
}