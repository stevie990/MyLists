package com.sserra.mylists.model

import androidx.recyclerview.selection.Selection
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.sserra.mylists.data.Item
import com.sserra.mylists.data.MyList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

@ExperimentalCoroutinesApi
class FirestoreService {

    private var firestore: FirebaseFirestore = Firebase.firestore

    fun getLists(): Flow<List<MyList>> = callbackFlow {
        val subscription = firestore.collection("lists")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Timber.w("Listen Failed")
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    val allLists = querySnapshot.toObjects<MyList>()
                    offer(allLists)
                }
            }

        awaitClose { subscription.remove() }
    }

    fun addNewList(list: MyList) {
        firestore.collection("lists").document(list.id.toString()).set(list)
    }

    fun getItems(listId: String): Flow<List<Item>> = callbackFlow {
        val subscription = firestore.collection("lists").document(listId).collection("items")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Timber.w("Listen Failed")
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    val allItems = querySnapshot.toObjects<Item>().sortedBy { it.title }
                    offer(allItems)
                }
            }

        awaitClose { subscription.remove() }
    }

    fun getListItemsCount(listId: String): Flow<Int> = callbackFlow {
        val subscription = firestore.collection("lists").document(listId).collection("items")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Timber.w("Listen Failed")
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    val listItemsCount = querySnapshot.count()
                    offer(listItemsCount)
                }
            }

        awaitClose { subscription.remove() }
    }

    fun addNewItem(listId: String, item: Item) {
        firestore.collection("lists").document(listId).collection("items")
            .document(item.id.toString())
            .set(item)
    }

    fun completeItem(listId: String, item: Item) {
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
