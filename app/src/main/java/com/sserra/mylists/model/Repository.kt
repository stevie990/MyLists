package com.sserra.mylists.model

import androidx.recyclerview.selection.Selection
import com.sserra.mylists.business.domain.model.Item
import com.sserra.mylists.business.domain.model.MyList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
class Repository @Inject constructor(
    val firestoreService: FirestoreServiceRepo
) {

    fun getLists() : Flow<List<MyList>> {
        return firestoreService.getLists()
    }

    suspend fun addNewList(list: MyList) {
        withContext(Dispatchers.IO) {
            firestoreService.addNewList(list)
        }
    }

    fun getListItemsCount(listId: String): Flow<Int> {
        return firestoreService.getListItemsCount(listId)
    }

    fun getItems(listId: String): Flow<List<Item>> {
        return firestoreService.getItems(listId)
    }

    suspend fun addNewItem(listId: String, item: Item) {
        withContext(Dispatchers.IO){
            firestoreService.addNewItem(listId, item)
        }
    }

    suspend fun completeItem(listId: String, item: Item) {
        withContext(Dispatchers.IO){
            firestoreService.completeItem(listId, item)
        }
    }

    suspend fun deleteItems(listId: String, selectedItems: Selection<String>) {
        withContext(Dispatchers.IO){
            firestoreService.deleteItems(listId, selectedItems)
        }
    }
}