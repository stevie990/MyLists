package com.sserra.mylists.business.data.network

import com.sserra.mylists.business.domain.model.Item
import com.sserra.mylists.business.domain.model.MyList

interface NetworkDataSource {

    // List
    suspend fun insertList(list: MyList)

    suspend fun getLists() : List<MyList>

    // Item
    suspend fun insertItem(item: Item)

    suspend fun getItems() : List<Item>
}