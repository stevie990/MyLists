package com.sserra.mylists.model

import androidx.recyclerview.selection.Selection
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.sserra.mylists.data.Item
import com.sserra.mylists.data.MyList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class FirestoreService {
    private var firestore: FirebaseFirestore = Firebase.firestore

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    @ExperimentalCoroutinesApi
    fun getLists(): Flow<List<MyList>> = callbackFlow {
        val subscription = firestore.collection("lists")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Timber.w("Listen Failed")
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val allLists = querySnapshot.toObjects<MyList>()
                    offer(allLists)
                }
            }

        awaitClose { subscription.remove() }
    }

    fun addNewList(list: MyList) {
        firestore.collection("lists").document(list.id.toString()).set(list)
    }

    @ExperimentalCoroutinesApi
    fun getItems(listId: String): Flow<List<Item>> = callbackFlow {
        val subscription = firestore.collection("lists").document(listId).collection("items")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Timber.w("Listen Failed")
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val allItems = querySnapshot.toObjects<Item>().sortedBy { it.title }
                    offer(allItems)
                }
            }

        awaitClose { subscription.remove() }
    }

    fun addNewItem(listId: String, item: Item) {
        firestore.collection("lists").document(listId).collection("items")
            .document(item.id.toString())
            .set(item)
    }

    fun completeItem(listId: String, item: Item, completed: Boolean) {
        firestore.collection("lists").document(listId).collection("items")
            .document(item.id.toString())
            .set(item)
            .addOnSuccessListener {
                Timber.i("Item ${item.title} completed")
            }
            .addOnFailureListener {
                Timber.i("Item ${item.title} completion failed")
            }
    }

    fun deleteItems(listId: String, selectedItems: Selection<String>) {
        selectedItems.forEach { itemId ->
            firestore.collection("lists").document(listId).collection("items").document(itemId)
                .delete()
        }
    }
}
