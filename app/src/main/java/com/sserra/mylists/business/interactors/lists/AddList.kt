package com.sserra.mylists.business.interactors.lists

import com.sserra.mylists.business.data.cache.CacheDataSource
import com.sserra.mylists.business.data.network.NetworkDataSource
import com.sserra.mylists.business.domain.model.MyList
import com.sserra.mylists.business.domain.state.DataState
import com.sserra.mylists.framework.presentation.lists.state.MyListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class AddList constructor(
    private val cacheDataSource: CacheDataSource,
    private val networkDataSource: NetworkDataSource
) {

    suspend fun execute(list: MyList): Flow<DataState<MyListViewState>> = flow {

        emit(DataState.loading(true))

        try {
            val cachedLists = withContext(Dispatchers.IO) {
                cacheDataSource.insertList(list)
                networkDataSource.insertList(list)
                cacheDataSource.getLists()
            }

            emit(DataState.data(null, MyListViewState(lists = cachedLists)))
        } catch (e: Exception) {
            emit(DataState.error(e.message.toString()))
        }
    }
}