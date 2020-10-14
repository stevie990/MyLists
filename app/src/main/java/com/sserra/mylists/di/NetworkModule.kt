package com.sserra.mylists.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sserra.mylists.business.data.network.NetworkDataSource
import com.sserra.mylists.business.data.network.NetworkDataSourceImplementation
import com.sserra.mylists.framework.datasource.network.abstraction.FirestoreService
import com.sserra.mylists.framework.datasource.network.implementation.FirestoreServiceImplementation
import com.sserra.mylists.framework.datasource.network.mappers.ItemNetworkMapper
import com.sserra.mylists.framework.datasource.network.mappers.MyListNetworkMapper
import com.sserra.mylists.model.FirestoreServiceRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    fun provideFirestoreService(
        firestore: FirebaseFirestore
    ) : FirestoreService {
        return FirestoreServiceImplementation(firestore)
    }

    @Singleton
    @Provides
    fun provideFirestoreServiceRepo(
        firestore: FirebaseFirestore,
        listNetworkMapper: MyListNetworkMapper,
        itemNetworkMapper: ItemNetworkMapper
    ) : FirestoreServiceRepo {
        return FirestoreServiceRepo(firestore, listNetworkMapper, itemNetworkMapper)
    }

    @Singleton
    @Provides
    fun provideNetworkDataSource(
        firestoreService: FirestoreService,
        listNetworkMapper: MyListNetworkMapper,
        itemNetworkMapper: ItemNetworkMapper
    ) : NetworkDataSource {
        return NetworkDataSourceImplementation(firestoreService, listNetworkMapper, itemNetworkMapper)
    }
}