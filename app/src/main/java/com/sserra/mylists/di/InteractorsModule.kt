package com.sserra.mylists.di

import com.sserra.mylists.business.data.cache.CacheDataSource
import com.sserra.mylists.business.data.network.NetworkDataSource
import com.sserra.mylists.business.interactors.items.AddItem
import com.sserra.mylists.business.interactors.items.GetItems
import com.sserra.mylists.business.interactors.lists.AddList
import com.sserra.mylists.business.interactors.lists.GetLists
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object InteractorsModule {

    @Singleton
    @Provides
    fun provideGetLists(
        cacheDataSource: CacheDataSource,
        networkDataSource: NetworkDataSource
    ) : GetLists {
        return GetLists(cacheDataSource, networkDataSource)
    }

    @Singleton
    @Provides
    fun provideAddList(
        cacheDataSource: CacheDataSource,
        networkDataSource: NetworkDataSource
    ) : AddList {
        return AddList(cacheDataSource, networkDataSource)
    }

    @Singleton
    @Provides
    fun provideGetItems(
        cacheDataSource: CacheDataSource,
        networkDataSource: NetworkDataSource
    ) : GetItems {
        return GetItems(cacheDataSource, networkDataSource)
    }

    @Singleton
    @Provides
    fun provideAddItem(
        cacheDataSource: CacheDataSource,
        networkDataSource: NetworkDataSource
    ) : AddItem {
        return AddItem(cacheDataSource, networkDataSource)
    }
}