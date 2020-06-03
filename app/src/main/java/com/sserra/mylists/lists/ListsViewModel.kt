package com.sserra.mylists.lists

import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sserra.mylists.data.MyList
import com.sserra.mylists.model.FirestoreService
import com.sserra.mylists.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class ListsViewModel : ViewModel() {

    private var firestore: FirebaseFirestore = Firebase.firestore
    private val firestoreService = FirestoreService()
    private val repository = Repository.getInstance(firestoreService)

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    @ExperimentalCoroutinesApi
    val lists = repository.getLists().asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)

    private val _openList = MutableLiveData<MyList>()
    val openList: LiveData<MyList>
        get() = _openList

    fun displayList(list: MyList){
        _openList.value = list
    }

    fun displayListComplete(){
        _openList.value = null
    }

    fun addNewList(list: MyList) {
        viewModelScope.launch {
            repository.addNewList(list)
        }
    }
}