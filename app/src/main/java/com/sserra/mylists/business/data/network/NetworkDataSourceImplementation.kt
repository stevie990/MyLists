package com.sserra.mylists.business.data.network

import com.sserra.mylists.business.domain.model.Item
import com.sserra.mylists.business.domain.model.MyList
import com.sserra.mylists.framework.datasource.network.abstraction.FirestoreService
import com.sserra.mylists.framework.datasource.network.mappers.ItemNetworkMapper
import com.sserra.mylists.framework.datasource.network.mappers.MyListNetworkMapper

class NetworkDataSourceImplementation constructor(
    private val firestoreService: FirestoreService,
    private val listNetworkMapper: MyListNetworkMapper,
    private val itemNetworkMapper: ItemNetworkMapper
) : NetworkDataSource {

    override suspend fun insertList(list: MyList) {
        firestoreService.insertList(listNetworkMapper.mapToEntity(list))
    }

    override suspend fun getLists(): List<MyList> {
        return listNetworkMapper.entityListToMyListList(firestoreService.getAllLists())
    }

    override suspend fun insertItem(item: Item, listId: String) {
        firestoreService.insertItem(itemNetworkMapper.mapToEntity(item), listId)
    }

    override suspend fun getItems(listId: String): List<Item> {
        return itemNetworkMapper.entityListToItemsList(firestoreService.getAllItems(listId))
    }

}