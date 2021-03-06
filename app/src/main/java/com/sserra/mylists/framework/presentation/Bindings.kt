package com.sserra.mylists.framework.presentation

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sserra.mylists.business.domain.model.Item
import com.sserra.mylists.business.domain.model.MyList
import com.sserra.mylists.framework.presentation.items.ItemsAdapter
import com.sserra.mylists.framework.presentation.lists.ListsAdapter

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

//@BindingAdapter("app:listItemsCount")
//fun setListItemsCount(textView: MaterialTextView, listId: String?) {
//    listId?.let {
//        textView.text =
//    }
//}