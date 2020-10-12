package com.sserra.mylists.business.domain.model

data class Item(
    var id: String,
    var listId: String = "",
    val title: String,
    val description: String,
    var isCompleted: Boolean,
    val updated_at: String,
    val created_at: String
){
    constructor(): this("", "", "", "", false, "", "")
}