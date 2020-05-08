package com.sserra.mylists

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sserra.mylists.data.Item
import com.sserra.mylists.data.MyList
import com.sserra.mylists.items.ItemsAdapter
import com.sserra.mylists.lists.ListsAdapter

/**
 * [BindingAdapter]s for the [List]s list.
 */
@BindingAdapter("app:lists")
fun setLists(listView: RecyclerView, lists: List<MyList>?) {
    lists?.let {
        (listView.adapter as ListsAdapter).submitList(lists)
    }
}

/**
 * [BindingAdapter]s for the [Item]s list.
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Item>?) {
    items?.let {
        (listView.adapter as ItemsAdapter).submitList(items)
    }
}