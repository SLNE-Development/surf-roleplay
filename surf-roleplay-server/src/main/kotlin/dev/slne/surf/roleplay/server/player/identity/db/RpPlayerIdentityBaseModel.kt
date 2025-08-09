package dev.slne.surf.roleplay.server.player.identity.db

import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.player.db.RpPlayerModel
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.id.EntityID

abstract class RpPlayerIdentityBaseModel<I : RpIdentity>(
    id: EntityID<Long>,
    table: RpPlayerIdentityBaseTable
) : LongEntity(id) {
    var player by RpPlayerModel referencedOn table.player

    var firstName by table.firstName
    var lastName by table.lastName
    var dateOfBirth by table.dateOfBirth

    var createdAt by table.createdAt
    var updatedAt by table.updatedAt

    abstract suspend fun toApi(player: RpPlayer): I
}