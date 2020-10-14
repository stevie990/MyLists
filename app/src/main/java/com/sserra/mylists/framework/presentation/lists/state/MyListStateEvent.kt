package com.sserra.mylists.framework.presentation.lists.state

import com.sserra.mylists.business.domain.model.MyList

sealed class MyListStateEvent {

    object GetMyListsEvent : MyListStateEvent()

    class AddListEvent(val list: MyList) : MyListStateEvent()

    object None: MyListStateEvent()
}