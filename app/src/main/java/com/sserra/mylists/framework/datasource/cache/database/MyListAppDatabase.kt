package com.sserra.mylists.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sserra.mylists.framework.datasource.cache.model.ItemCacheEntity
import com.sserra.mylists.framework.datasource.cache.model.MyListCacheEntity

@Database(entities = [MyListCacheEntity::class, ItemCacheEntity::class], version = 2)
abstract class MyListAppDatabase : RoomDatabase() {

    abstract fun myListDao() : MyListDao

    abstract fun itemDao() : ItemDao

    companion object{
        const val DATABASE_NAME = "myList_db"
    }
}