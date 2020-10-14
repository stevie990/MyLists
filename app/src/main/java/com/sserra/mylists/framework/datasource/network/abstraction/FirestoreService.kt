package com.sserra.mylists.framework.datasource.network.abstraction

import com.sserra.mylists.framework.datasource.network.model.ItemNetworkEntity
import com.sserra.mylists.framework.datasource.network.model.MyListNetworkEntity


interface FirestoreService {

    // List
    suspend fun insertList(listNetworkEntity: MyListNetworkEntity)

    suspend fun getAllLists(): List<MyListNetworkEntity>

    // Item
    suspend fun insertItem(itemNetworkEntity: ItemNetworkEntity)

    suspend fun getAllItems(): List<ItemNetworkEntity>
}