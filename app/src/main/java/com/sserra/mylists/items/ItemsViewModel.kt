package com.sserra.mylists.items

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.sserra.mylists.data.Item

class ItemsViewModel() : ViewModel() {

    private var firestore : FirebaseFirestore

    private var _items: MutableLiveData<ArrayList<Item>> = MutableLiveData<ArrayList<Item>>()
    val items: LiveData<ArrayList<Item>>
        get() = _items

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToItems()
    }

    private fun listenToItems() {
        firestore.collection("items").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null){
                Log.w("TAG", "Listen Failed", firebaseFirestoreException)
                return@addSnapshotListener
            }

            if (querySnapshot != null){
                val allItems = ArrayList<Item>()
                val documents = querySnapshot.documents
                documents.forEach {
                    val item = it.toObject(Item::class.java)
                    if (item != null){
                        allItems.add(item)
                    }
                }

                _items.value = allItems
            }
        }
    }

//    fun openItem(itemId: String){
//        // Open an item here
//    }

}