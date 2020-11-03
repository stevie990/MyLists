package com.sserra.mylists.business.interactors.lists

import com.sserra.mylists.business.data.cache.CacheDataSource
import com.sserra.mylists.business.data.network.NetworkDataSource
import com.sserra.mylists.business.domain.state.DataState
import com.sserra.mylists.framework.presentation.lists.state.MyListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext


class GetLists constructor(
    private val cacheDataSource: CacheDataSource,
    private val networkDataSource: NetworkDataSource
) {

    suspend fun execute(): Flow<DataState<MyListViewState>> = flow {

        emit(DataState.loading(true))

        try {
            val cachedLists = withContext(Dispatchers.IO) {
                val networkLists = networkDataSource.getLists()
                cacheDataSource.insertListOfLists(networkLists)
                cacheDataSource.getLists()
            }

            emit(DataState.data(null, MyListViewState(lists = cachedLists)))
        } catch (e: Exception) {
            emit(DataState.error(e.message.toString()))
        }
    }
}