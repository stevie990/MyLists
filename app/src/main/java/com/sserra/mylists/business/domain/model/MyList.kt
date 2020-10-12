package com.sserra.mylists.business.domain.model

data class MyList(
    var id: String,
    val title: String,
    val description: String,
    val updated_at: String,
    val created_at: String
) {
    constructor(): this("", "", "", "", "")
}