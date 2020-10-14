package com.sserra.mylists.framework.presentation.lists.state

import androidx.lifecycle.LiveData
import com.sserra.mylists.business.domain.model.MyList

data class MyListViewState(
    var lists: LiveData<List<MyList>>? = null
)