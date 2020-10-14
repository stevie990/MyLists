package com.sserra.mylists.business.data.cache

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.sserra.mylists.business.domain.model.Item
import com.sserra.mylists.business.domain.model.MyList
import com.sserra.mylists.framework.datasource.cache.abstraction.DaoService
import com.sserra.mylists.framework.datasource.cache.mappers.ItemCacheMapper
import com.sserra.mylists.framework.datasource.cache.mappers.MyListCacheMapper

class CacheDataSourceImplementation constructor(
    private val daoService: DaoService,
    private val listMapper: MyListCacheMapper,
    private val itemMapper: ItemCacheMapper
) : CacheDataSource {

    // List
    override suspend fun insertList(myList: MyList): Long {
        return daoService.insertList(listMapper.mapToEntity(myList))
    }

    override fun getLists(): LiveData<List<MyList>> {
        return daoService.getAllLists().map {
            listMapper.entityListToMyListList(it)
        }
    }

    override suspend fun insertListOfLists(lists: List<MyList>) {
        lists.forEach {list ->
            daoService.insertList(listMapper.mapToEntity(list))
        }
    }

    // Item
    override suspend fun insertItem(item: Item): Long {
        return daoService.insertItem(itemMapper.mapToEntity(item))
    }

    override suspend fun getItems(): List<Item> {
        return itemMapper.entityListToItemList(daoService.getAllItems())
    }

}