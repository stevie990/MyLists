package com.sserra.mylists.data

data class Item(
    val title: String? = null,
    val description: String? = null,
    var isCompleted: Boolean? = false,
    var id: String? = null
)