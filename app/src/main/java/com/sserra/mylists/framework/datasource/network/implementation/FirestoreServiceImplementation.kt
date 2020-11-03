package com.sserra.mylists.framework.datasource.network.implementation

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.sserra.mylists.framework.datasource.network.abstraction.FirestoreService
import com.sserra.mylists.framework.datasource.network.model.ItemNetworkEntity
import com.sserra.mylists.framework.datasource.network.model.MyListNetworkEntity
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirestoreServiceImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirestoreService {
    override suspend fun insertList(listNetworkEntity: MyListNetworkEntity) {
        firestore.collection("lists").document(listNetworkEntity.id).set(listNetworkEntity)
    }

    override suspend fun getAllLists(): List<MyListNetworkEntity> {
        return firestore.collection("lists").get()
            .addOnFailureListener {
                Timber.e(it)
            }
            .await()
            .toObjects(MyListNetworkEntity::class.java)
    }

    override suspend fun insertItem(itemNetworkEntity: ItemNetworkEntity, listId: String) {
        firestore.collection("lists").document(listId).collection("items")
            .document(itemNetworkEntity.id)
            .set(itemNetworkEntity)
    }

    override suspend fun getAllItems(listId: String): List<ItemNetworkEntity> {
        return firestore.collection("lists").document(listId).collection("items")
            .get()
            .addOnFailureListener {
                Timber.e(it)
            }
            .await()
            .toObjects(ItemNetworkEntity::class.java)
    }
}