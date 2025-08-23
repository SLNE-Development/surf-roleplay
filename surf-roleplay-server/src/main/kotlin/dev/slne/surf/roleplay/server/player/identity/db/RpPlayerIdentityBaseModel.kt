package dev.slne.surf.roleplay.server.player.identity.db

import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongEntity
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongEntityClass
import dev.slne.surf.roleplay.core.player.db.RpPlayerModel
import org.jetbrains.exposed.dao.id.EntityID

abstract class RpPlayerIdentityBaseModel(
    id: EntityID<Long>,
    table: RpPlayerIdentityBaseTable
) : AuditableLongEntity(id, table) {
    var player by RpPlayerModel referencedOn table.player

    var firstName by table.firstName
    var lastName by table.lastName
    var dateOfBirth by table.dateOfBirth
}

abstract class RpPlayerIdentityBaseClass<out E : RpPlayerIdentityBaseModel>(val identityTable: RpPlayerIdentityBaseTable) :
    AuditableLongEntityClass<E>(identityTable)