package com.sserra.mylists.business.domain.model

data class MyList(
    var id: String,
    val title: String,
    val description: String,
    val created_at: String,
    val updated_at: String
) {
    constructor(): this("", "", "", "", "")
}