package com.sserra.mylists.framework.presentation.items.state

import com.sserra.mylists.business.domain.model.Item

sealed class ItemStateEvent {

    object GetItemsEvent: ItemStateEvent()

    class AddItemEvent(val item: Item) : ItemStateEvent()

    object None: ItemStateEvent()
}