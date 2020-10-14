package com.sserra.mylists.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "listId")
    var listId: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "isCompleted")
    var isCompleted: Boolean,

    @ColumnInfo(name = "created_at")
    var created_at: String,

    @ColumnInfo(name = "updated_at")
    var updated_at: String
)