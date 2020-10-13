package com.sserra.mylists.framework.datasource.network.mappers

import com.sserra.mylists.business.domain.model.MyList
import com.sserra.mylists.business.domain.util.DateUtil
import com.sserra.mylists.business.domain.util.EntityMapper
import com.sserra.mylists.framework.datasource.network.model.MyListNetworkEntity
import javax.inject.Inject

class MyListNetworkMapper @Inject constructor(
    private val dateUtil: DateUtil
) : EntityMapper<MyListNetworkEntity, MyList> {

    override fun mapFromEntity(entity: MyListNetworkEntity): MyList {
        return MyList(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            created_at = dateUtil.convertFirebaseTimestampToStringData(entity.created_at),
            updated_at = dateUtil.convertFirebaseTimestampToStringData(entity.updated_at)
            )
    }

    override fun mapToEntity(domainModel: MyList): MyListNetworkEntity {
        return MyListNetworkEntity(
            id = domainModel.id,
            title = domainModel.title,
            description = domainModel.description,
            created_at = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.created_at),
            updated_at = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updated_at)
        )
    }

    fun entityListToMyListList(entities: List<MyListNetworkEntity>) : List<MyList> {
        return entities.map { mapFromEntity(it) }
    }

    fun myListListToEntityList(myListList: List<MyList>): List<MyListNetworkEntity> {
        return myListList.map { mapToEntity(it) }
    }

}