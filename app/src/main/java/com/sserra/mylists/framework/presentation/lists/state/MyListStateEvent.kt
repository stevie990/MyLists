package com.sserra.mylists.framework.presentation.lists.state

sealed class MyListStateEvent {

    object GetMyListsEvent : MyListStateEvent()

    object None: MyListStateEvent()
}