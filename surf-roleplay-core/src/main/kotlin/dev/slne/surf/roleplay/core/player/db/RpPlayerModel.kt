package dev.slne.surf.roleplay.core.player.db

import dev.slne.surf.roleplay.core.player.RpPlayerImpl
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RpPlayerModel(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<RpPlayerModel>(RpPlayerTable)

    var uuid by RpPlayerTable.uuid
    var username by RpPlayerTable.username

    var firstName by RpPlayerTable.firstName
    var lastName by RpPlayerTable.lastName
    var birthDate by RpPlayerTable.birthDate

    var createdAt by RpPlayerTable.createdAt
    var updatedAt by RpPlayerTable.updatedAt

    fun toApi() = RpPlayerImpl(
        uuid = uuid,
    ).apply {
        this.username = this@RpPlayerModel.username
        this.information.firstName = this@RpPlayerModel.firstName
        this.information.lastName = this@RpPlayerModel.lastName
        this.information.birthDate = this@RpPlayerModel.birthDate
        this.createdAt = createdAt
        this.updatedAt = updatedAt
    }

}