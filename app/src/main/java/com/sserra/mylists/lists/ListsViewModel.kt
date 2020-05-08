package com.sserra.mylists.lists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.sserra.mylists.data.MyList
import timber.log.Timber

class ListsViewModel() : ViewModel() {

    private var firestore: FirebaseFirestore

    private var _lists: MutableLiveData<ArrayList<MyList>> = MutableLiveData<ArrayList<MyList>>()
    val lists: LiveData<ArrayList<MyList>>
        get() = _lists

    private val _openList = MutableLiveData<String>()
    val openList: LiveData<String>
        get() = _openList

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToLists()
    }

    private fun listenToLists() {
        firestore.collection("lists").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null){
                Timber.w("Listen Failed")
//                Log.w("TAG", "Listen Failed", firebaseFirestoreException)
                return@addSnapshotListener
            }

            if (querySnapshot != null){
                val allLists = ArrayList<MyList>()
                val documents = querySnapshot.documents
                documents.forEach {
                    val list = it.toObject(MyList::class.java)
                    if (list != null){
                        allLists.add(list)
                    }
                }

                _lists.value = allLists
            }
        }
    }

    fun displayList(listId: String){
        _openList.value = listId
    }
}