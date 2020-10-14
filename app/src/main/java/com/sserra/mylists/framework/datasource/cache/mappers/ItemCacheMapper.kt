package com.sserra.mylists.framework.datasource.cache.mappers

import com.sserra.mylists.business.domain.model.Item
import com.sserra.mylists.business.domain.util.EntityMapper
import com.sserra.mylists.framework.datasource.cache.model.ItemCacheEntity
import javax.inject.Inject

class ItemCacheMapper @Inject constructor(
) : EntityMapper<ItemCacheEntity, Item> {

    override fun mapFromEntity(entity: ItemCacheEntity): Item {
        return Item(
            id = entity.id,
            listId = entity.listId,
            title = entity.title,
            description = entity.description,
            isCompleted = entity.isCompleted,
            created_at = entity.created_at,
            updated_at = entity.updated_at
            )
    }

    override fun mapToEntity(domainModel: Item): ItemCacheEntity {
        return ItemCacheEntity(
            id = domainModel.id,
            listId = domainModel.listId,
            title = domainModel.title,
            description = domainModel.description,
            isCompleted = domainModel.isCompleted,
            created_at = domainModel.created_at,
            updated_at = domainModel.updated_at
        )
    }

    fun entityListToItemList(entities: List<ItemCacheEntity>) : List<Item> {
        return entities.map { mapFromEntity(it) }
    }

    fun itemListToEntityList(myListList: List<Item>): List<ItemCacheEntity> {
        return myListList.map { mapToEntity(it) }
    }
}