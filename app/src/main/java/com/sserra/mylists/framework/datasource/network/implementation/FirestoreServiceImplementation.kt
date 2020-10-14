package com.sserra.mylists.framework.datasource.network.implementation

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
    override suspend fun insertList(listCacheEntity: MyListNetworkEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLists(): List<MyListNetworkEntity> {
        return firestore.collection("lists").get()
            .addOnFailureListener {
                Timber.e(it)
            }
            .await()
            .toObjects(MyListNetworkEntity::class.java)
    }

    override suspend fun insertItem(itemCacheEntity: ItemNetworkEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllItems(): List<ItemNetworkEntity> {
        TODO("Not yet implemented")
    }
}