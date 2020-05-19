package com.sserra.mylists.items

import android.content.Context
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
//    private val listItems:List<Item>, I think ListAdapter is providing getCurrentList
//    private val context: Context, same here
    ) :
    ListAdapter<Item, ItemsAdapter.ViewHolder>(TaskDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    private var tracker: SelectionTracker<Long>? = null

    fun setTracker(tracker: SelectionTracker<Long>?) {
        this.tracker = tracker
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)

        if(tracker!!.isSelected(position.toLong())) {
            holder.binding.itemLayout.background = ColorDrawable(Color.parseColor("#80deea")
            )
        } else {
            // Reset color to white if not selected
            holder.binding.itemLayout.background = ColorDrawable(Color.WHITE)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemsViewModel, item: Item) {
            binding.viewmodel = viewModel
            binding.item = item
            binding.executePendingBindings()
        }

        // Method for recyclerview.selection
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition

                override fun getSelectionKey(): Long? = itemId
            }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
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