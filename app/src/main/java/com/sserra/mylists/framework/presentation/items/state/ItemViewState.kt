package com.sserra.mylists.framework.presentation.items.state

import androidx.lifecycle.LiveData
import com.sserra.mylists.business.domain.model.Item

data class ItemViewState(
    var items: LiveData<List<Item>>? = null
)