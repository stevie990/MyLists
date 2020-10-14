package com.sserra.mylists.framework.datasource.cache.implementation

import androidx.lifecycle.LiveData
import com.sserra.mylists.framework.datasource.cache.abstraction.DaoService
import com.sserra.mylists.framework.datasource.cache.database.ItemDao
import com.sserra.mylists.framework.datasource.cache.database.MyListDao
import com.sserra.mylists.framework.datasource.cache.model.ItemCacheEntity
import com.sserra.mylists.framework.datasource.cache.model.MyListCacheEntity

class DaoServiceImplementation constructor(
    private val listDao: MyListDao,
    private val itemDao: ItemDao
) : DaoService {

    // List
    override suspend fun insertList(listCacheEntity: MyListCacheEntity): Long {
        return listDao.insert(listCacheEntity)
    }

    override fun getAllLists(): LiveData<List<MyListCacheEntity>> {
        return listDao.getAllLists()
    }

    // Item
    override suspend fun insertItem(itemCacheEntity: ItemCacheEntity): Long {
        return itemDao.insert(itemCacheEntity)
    }

    override suspend fun getAllItems(): List<ItemCacheEntity> {
        TODO("Not yet implemented")
    }


}