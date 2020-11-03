package com.sserra.mylists.business.interactors.items

import com.sserra.mylists.business.data.cache.CacheDataSource
import com.sserra.mylists.business.data.network.NetworkDataSource
import com.sserra.mylists.business.domain.state.DataState
import com.sserra.mylists.framework.presentation.items.state.ItemViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class GetItems constructor(
    private val cacheDataSource: CacheDataSource,
    private val networkDataSource: NetworkDataSource
) {

    suspend fun execute(listId: String): Flow<DataState<ItemViewState>> = flow {

        emit(DataState.loading(true))

        try {
            val cachedItems = withContext(Dispatchers.IO) {
                val networkItems = networkDataSource.getItems(listId)
                cacheDataSource.insertListOfItems(networkItems)
                cacheDataSource.getItems(listId)
            }

            emit(DataState.data(null, ItemViewState(items = cachedItems)))
        } catch (e: Exception) {
            emit(DataState.error(e.message.toString()))
        }
    }
}