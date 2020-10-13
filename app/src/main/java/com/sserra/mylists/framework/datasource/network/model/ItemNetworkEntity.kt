package com.sserra.mylists.framework.datasource.network.model

import com.google.firebase.Timestamp

data class ItemNetworkEntity(
    var id: String,
    var listId: String,
    val title: String,
    val description: String,
    var isCompleted: Boolean,
    val updated_at: Timestamp,
    val created_at: Timestamp
){
    constructor(): this("", "", "", "", false, Timestamp.now(), Timestamp.now())
}