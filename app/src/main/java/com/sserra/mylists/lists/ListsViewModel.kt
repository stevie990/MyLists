package com.sserra.mylists.lists

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sserra.mylists.data.MyList
import com.sserra.mylists.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ListsViewModel @ViewModelInject constructor(
    val repository: Repository
) : ViewModel() {

    private var firestore: FirebaseFirestore = Firebase.firestore

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    val lists = repository.getLists().asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)

    private val _openList = MutableLiveData<MyList>()
    val openList: LiveData<MyList>
        get() = _openList

    fun displayList(list: MyList) {
        _openList.value = list
    }

    fun displayListComplete() {
        _openList.value = null
    }

    fun getListItemsCount(listId: String): LiveData<Int> {
        return repository.getListItemsCount(listId)
            .asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)
    }

    fun addNewList(list: MyList) {
        viewModelScope.launch {
            repository.addNewList(list)
        }
    }
}