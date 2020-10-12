package com.sserra.mylists.framework.presentation.lists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sserra.mylists.business.domain.model.MyList
import com.sserra.mylists.databinding.ListItemBinding

class ListsAdapter(private val viewModel: ListsViewModel) : ListAdapter<MyList, ListsAdapter.ViewHolder>(
    TaskDiffCallback()
){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(viewModel: ListsViewModel, list: MyList){
            binding.viewmodel = viewModel
            binding.list = list
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<MyList>() {
    override fun areItemsTheSame(oldItem: MyList, newItem: MyList): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MyList, newItem: MyList): Boolean {
        return oldItem == newItem
    }
}