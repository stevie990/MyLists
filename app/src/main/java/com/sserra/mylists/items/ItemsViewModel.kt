package com.sserra.mylists.items

import androidx.lifecycle.*
import androidx.recyclerview.selection.Selection
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sserra.mylists.data.Item
import com.sserra.mylists.model.FirestoreService
import com.sserra.mylists.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class ItemsViewModel(private val listId: String) : ViewModel() {

    private var firestore: FirebaseFirestore = Firebase.firestore
    private val firestoreService = FirestoreService()
    private val repository = Repository.getInstance(firestoreService)

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    @ExperimentalCoroutinesApi
    val items = repository.getItems(listId).asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)

    fun addNewItem(item: Item) {
        viewModelScope.launch {
            repository.addNewItem(listId, item)
        }
    }

    fun completeItem(item: Item, completed: Boolean) {
        viewModelScope.launch {
            repository.completeItem(listId, item, completed)
        }
    }

    fun deleteItems(selectedItems: Selection<String>) {
        viewModelScope.launch {
            repository.deleteItems(listId, selectedItems)
        }
    }
}