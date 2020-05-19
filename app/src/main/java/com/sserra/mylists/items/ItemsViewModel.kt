package com.sserra.mylists.items

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.selection.Selection
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sserra.mylists.data.Item
import timber.log.Timber

class ItemsViewModel() : ViewModel() {

    private var firestore: FirebaseFirestore
    private lateinit var listId: String

    private var _items: MutableLiveData<ArrayList<Item>> = MutableLiveData<ArrayList<Item>>()
    val items: LiveData<ArrayList<Item>>
        get() = _items

    init {
        firestore = Firebase.firestore
//            FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    private fun listenToItems(listId: String) {
        this.listId = listId
        firestore.collection("lists").document(listId).collection("items")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.w("TAG", "Listen Failed", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val allItems = ArrayList<Item>()
                    val documents = querySnapshot.documents
                    documents.forEach {
                        val item = it.toObject(Item::class.java)
                        if (item != null) {
                            allItems.add(item)
                        }
                    }

                    _items.value = allItems
                }
            }
    }

    fun setListId(listId: String) {
        listenToItems(listId)
    }

    fun addNewItem(item: Item) {
        firestore.collection("lists").document(listId).collection("items").document(item.id.toString())
            .set(item)
    }

    fun completeItem(item: Item, completed: Boolean){
        item.isCompleted = completed
        firestore.collection("lists").document(listId).collection("items").document(item.id.toString())
            .set(item)
            .addOnSuccessListener {
                Timber.i("Item ${item.title} completed") }
            .addOnFailureListener {
                Timber.i("Item ${item.title} completion failed") }
    }

    fun deleteItems(selectedItems: Selection<Item>){

    }
}