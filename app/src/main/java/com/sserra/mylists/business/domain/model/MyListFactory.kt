package com.sserra.mylists.business.domain.model

import com.sserra.mylists.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyListFactory @Inject constructor(
    private val dateUtil: DateUtil
) {
    fun createList(id: String? = null, title: String, description: String) : MyList {
        return MyList(
            id = id ?: UUID.randomUUID().toString(),
            title = title,
            description = description,
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }
}