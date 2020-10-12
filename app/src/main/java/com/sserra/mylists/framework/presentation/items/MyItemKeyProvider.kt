package com.sserra.mylists.framework.presentation.items

import androidx.recyclerview.selection.ItemKeyProvider

class MyItemKeyProvider(private val adapter: ItemsAdapter)
    : ItemKeyProvider<String>(SCOPE_CACHED) {

    override fun getKey(position: Int): String? {
        return adapter.currentList[position].id
    }

    override fun getPosition(key: String): Int {
        return adapter.currentList.indexOfFirst {
            it.id == key
        }
    }
}