package com.sserra.mylists.business.domain.model

import com.sserra.mylists.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemFactory @Inject constructor(
    private val dateUtil: DateUtil
) {
    fun createSingleItem(
        id: String? = null,
        title: String,
        description: String
    ) : Item {
        return Item(
            id = id ?: UUID.randomUUID().toString(),
            title = title,
            description = description,
            isCompleted = false,
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }
}