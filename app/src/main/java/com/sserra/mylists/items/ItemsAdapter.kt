package com.sserra.mylists.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sserra.mylists.data.Item
import com.sserra.mylists.databinding.ItemItemBinding

class ItemsAdapter(private val viewModel: ItemsViewModel) : ListAdapter<Item, ItemsAdapter.ViewHolder>(TaskDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(viewModel: ItemsViewModel, item: Item){
            binding.viewmodel = viewModel
            binding.item = item
            binding.executePendingBindings()
        }

        companion object{
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