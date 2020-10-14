package com.sserra.mylists.framework.datasource.network.model

import com.google.firebase.Timestamp

data class MyListNetworkEntity(
    var id: String,
    val title: String,
    val description: String,
    var updated_at: Timestamp,
    val created_at: Timestamp
) {
    constructor(): this("", "", "", Timestamp.now(), Timestamp.now())
}