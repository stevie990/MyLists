package com.sserra.mylists.di

import android.content.Context
import androidx.room.Room
import com.sserra.mylists.business.data.cache.CacheDataSource
import com.sserra.mylists.business.data.cache.CacheDataSourceImplementation
import com.sserra.mylists.framework.datasource.cache.abstraction.DaoService
import com.sserra.mylists.framework.datasource.cache.database.ItemDao
import com.sserra.mylists.framework.datasource.cache.database.MyListAppDatabase
import com.sserra.mylists.framework.datasource.cache.database.MyListDao
import com.sserra.mylists.framework.datasource.cache.implementation.DaoServiceImplementation
import com.sserra.mylists.framework.datasource.cache.mappers.ItemCacheMapper
import com.sserra.mylists.framework.datasource.cache.mappers.MyListCacheMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object CacheModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : MyListAppDatabase {
        return Room.databaseBuilder(
            context,
            MyListAppDatabase::class.java,
            MyListAppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMyListDAO(database: MyListAppDatabase) : MyListDao {
        return database.myListDao()
    }

    @Singleton
    @Provides
    fun provideItemDAO(database: MyListAppDatabase) : ItemDao {
        return database.itemDao()
    }

    @Singleton
    @Provides
    fun provideDaoService(
        listDao: MyListDao,
        itemDao: ItemDao
    ) : DaoService {
        return DaoServiceImplementation(listDao, itemDao)
    }

    @Singleton
    @Provides
    fun provideCacheDataSource(
        daoService: DaoService,
        listMapper: MyListCacheMapper,
        itemMapper: ItemCacheMapper
    ) : CacheDataSource {
        return CacheDataSourceImplementation(daoService, listMapper, itemMapper)
    }
}