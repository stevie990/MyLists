package com.sserra.mylists.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListsViewModelFactory : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListsViewModel() as T
        }

        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}