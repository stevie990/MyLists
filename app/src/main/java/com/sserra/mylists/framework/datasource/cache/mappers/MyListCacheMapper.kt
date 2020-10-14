package com.sserra.mylists.framework.datasource.cache.mappers

import com.sserra.mylists.business.domain.model.Item
import com.sserra.mylists.business.domain.model.MyList
import com.sserra.mylists.business.domain.util.EntityMapper
import com.sserra.mylists.framework.datasource.cache.model.ItemCacheEntity
import com.sserra.mylists.framework.datasource.cache.model.MyListCacheEntity
import javax.inject.Inject

class MyListCacheMapper @Inject constructor(
) : EntityMapper<MyListCacheEntity, MyList> {

    override fun mapFromEntity(entity: MyListCacheEntity): MyList {
        return MyList(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            created_at = entity.created_at,
            updated_at = entity.updated_at
        )
    }

    override fun mapToEntity(domainModel: MyList): MyListCacheEntity {
        return MyListCacheEntity(
            id = domainModel.id,
            title = domainModel.title,
            description = domainModel.description,
            created_at = domainModel.created_at,
            updated_at = domainModel.updated_at
        )
    }

    fun entityListToMyListList(entities: List<MyListCacheEntity>) : List<MyList> {
        return entities.map { mapFromEntity(it) }
    }

    fun myListListToEntityList(myListList: List<MyList>): List<MyListCacheEntity> {
        return myListList.map { mapToEntity(it) }
    }
}