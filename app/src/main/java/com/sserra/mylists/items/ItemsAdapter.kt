package com.sserra.mylists.items

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sserra.mylists.data.Item
import com.sserra.mylists.databinding.ItemItemBinding

class ItemsAdapter(
    private val viewModel: ItemsViewModel
) : ListAdapter<Item, ItemsAdapter.ViewHolder>(TaskDiffCallback()) {

//    init {
//        setHasStableIds(true)
//    }

    private var tracker: SelectionTracker<String>? = null

    fun setTracker(tracker: SelectionTracker<String>?) {
        this.tracker = tracker
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

//    override fun getItemViewType(position: Int): Int {
//        return position
//    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)

        if (tracker!!.isSelected(item.id)) {
            holder.binding.itemLayout.background = ColorDrawable(
                Color.parseColor("#9ccc65")
            )
        } else {
            // Reset color to white if not selected
            holder.binding.itemLayout.background = ColorDrawable(Color.WHITE)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
//        return ViewHolder.from(parent)
    }

    inner class ViewHolder constructor(val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemsViewModel, item: Item) {
            binding.viewmodel = viewModel
            binding.item = item
            binding.executePendingBindings()
        }

        // Method for recyclerview.selection
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<String> =
            object : ItemDetailsLookup.ItemDetails<String>() {
                override fun getPosition(): Int = adapterPosition

                override fun getSelectionKey(): String? = getItem(adapterPosition).id
            }

//        companion object {
//            fun from(parent: ViewGroup): ViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ItemItemBinding.inflate(layoutInflater, parent, false)
//
//                return ViewHolder(binding)
//            }
//        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}