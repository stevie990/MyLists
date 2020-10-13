package com.sserra.mylists.framework.datasource.network.mappers

import com.sserra.mylists.business.domain.model.Item
import com.sserra.mylists.business.domain.util.DateUtil
import com.sserra.mylists.business.domain.util.EntityMapper
import com.sserra.mylists.framework.datasource.network.model.ItemNetworkEntity
import javax.inject.Inject

class ItemNetworkMapper @Inject constructor(
    private val dateUtil: DateUtil
) : EntityMapper<ItemNetworkEntity, Item> {

    override fun mapFromEntity(entity: ItemNetworkEntity): Item {
        return Item(
            id = entity.id,
            listId = entity.listId,
            title = entity.title,
            description = entity.description,
            isCompleted = entity.isCompleted,
            created_at = dateUtil.convertFirebaseTimestampToStringData(entity.created_at),
            updated_at = dateUtil.convertFirebaseTimestampToStringData(entity.updated_at)
        )
    }

    override fun mapToEntity(domainModel: Item): ItemNetworkEntity {
        return ItemNetworkEntity(
            id = domainModel.id,
            listId = domainModel.listId,
            title = domainModel.title,
            description = domainModel.description,
            isCompleted = domainModel.isCompleted,
            created_at = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.created_at),
            updated_at = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updated_at)
        )
    }

    fun entityListToItemsList(entities: List<ItemNetworkEntity>) : List<Item> {
        return entities.map { mapFromEntity(it) }
    }

    fun itemListToEntityList(myListList: List<Item>): List<ItemNetworkEntity> {
        return myListList.map { mapToEntity(it) }
    }

}