package com.sserra.mylists.framework.presentation.items

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.recyclerview.selection.Selection
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sserra.mylists.business.domain.model.Item
import com.sserra.mylists.business.domain.model.ItemFactory
import com.sserra.mylists.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ItemsViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val itemFactory: ItemFactory,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val listId: String =
        savedStateHandle["listId"] ?: throw kotlin.IllegalArgumentException("List id required")

    private var firestore: FirebaseFirestore = Firebase.firestore

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    val items =
        repository.getItems(listId).asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)

    fun addNewItem(item: Item) {
        viewModelScope.launch {
            repository.addNewItem(listId, item)
        }
    }

    fun completeItem(item: Item, completed: Boolean) {
        item.isCompleted = completed
        viewModelScope.launch {
            repository.completeItem(listId, item)
        }
    }

    fun deleteItems(selectedItems: Selection<String>) {
        viewModelScope.launch {
            repository.deleteItems(listId, selectedItems)
        }
    }

    fun createNewItem(
        id: String,
        title: String,
        description: String
    ) = itemFactory.createSingleItem(id, title, description)
}