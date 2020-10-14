package com.sserra.mylists.framework.datasource.cache.abstraction

import androidx.lifecycle.LiveData
import com.sserra.mylists.framework.datasource.cache.model.ItemCacheEntity
import com.sserra.mylists.framework.datasource.cache.model.MyListCacheEntity

interface DaoService {

    // List
    suspend fun insertList(listCacheEntity: MyListCacheEntity): Long

    fun getAllLists(): LiveData<List<MyListCacheEntity>>

    // Item
    suspend fun insertItem(itemCacheEntity: ItemCacheEntity): Long

    suspend fun getAllItems(): List<ItemCacheEntity>
}