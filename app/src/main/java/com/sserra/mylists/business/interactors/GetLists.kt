package com.sserra.mylists.business.interactors

import com.sserra.mylists.business.data.cache.CacheDataSource
import com.sserra.mylists.business.data.network.NetworkDataSource
import com.sserra.mylists.business.domain.model.MyList
import com.sserra.mylists.business.domain.state.DataState
import com.sserra.mylists.framework.presentation.lists.state.MyListViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetLists constructor(
    private val cacheDataSource: CacheDataSource,
    private val networkDataSource: NetworkDataSource
) {

    suspend fun execute(): Flow<DataState<MyListViewState>> = flow {

        emit(DataState.loading(true))

        try {
            val networkLists = networkDataSource.getLists()
            cacheDataSource.insertListOfLists(networkLists)
            val cachedLists = cacheDataSource.getLists()

            emit(DataState.data(null, MyListViewState(lists = cachedLists)))
        } catch (e: Exception) {
            emit(DataState.error(e.message.toString()))
        }
    }
}