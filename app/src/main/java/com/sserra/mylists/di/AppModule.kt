package com.sserra.mylists.di

import com.sserra.mylists.business.domain.model.ItemFactory
import com.sserra.mylists.business.domain.model.MyListFactory
import com.sserra.mylists.business.domain.util.DateUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideDateFormat(): SimpleDateFormat {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("UTC-2") // match firestore
        return sdf
    }

    @Singleton
    @Provides
    fun provideDateUtil(dateFormat: SimpleDateFormat): DateUtil {
        return DateUtil(
            dateFormat
        )
    }

    @Singleton
    @Provides
    fun provideItemFactory(dateUtil: DateUtil) : ItemFactory = ItemFactory(dateUtil)

    @Singleton
    @Provides
    fun provideMyListFactory(dateUtil: DateUtil) : MyListFactory = MyListFactory(dateUtil)

}