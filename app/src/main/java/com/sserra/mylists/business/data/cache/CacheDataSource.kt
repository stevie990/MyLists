package com.sserra.mylists.business.data.cache

import androidx.lifecycle.LiveData
import com.sserra.mylists.business.domain.model.Item
import com.sserra.mylists.business.domain.model.MyList

interface CacheDataSource {

    // List
    suspend fun insertList(myList: MyList): Long

    fun getLists(): LiveData<List<MyList>>

    suspend fun insertListOfLists(lists: List<MyList>)

    // Item
    suspend fun insertItem(item: Item): Long

    fun getItems(listId: String): LiveData<List<Item>>

    suspend fun insertListOfItems(items: List<Item>)
}