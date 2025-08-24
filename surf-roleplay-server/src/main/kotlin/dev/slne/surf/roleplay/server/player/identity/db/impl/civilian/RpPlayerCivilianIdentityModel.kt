package dev.slne.surf.roleplay.server.player.identity.db.impl.civilian

import dev.slne.surf.roleplay.core.common.player.identity.NetworkIdentity
import dev.slne.surf.roleplay.server.player.identity.db.RpPlayerIdentityBaseClass
import dev.slne.surf.roleplay.server.player.identity.db.RpPlayerIdentityBaseModel
import org.jetbrains.exposed.dao.id.EntityID

class RpPlayerCivilianIdentityModel(id: EntityID<Long>) :
    RpPlayerIdentityBaseModel(id, RpPlayerCivilianIdentityTable) {
    companion object :
        RpPlayerIdentityBaseClass<RpPlayerCivilianIdentityModel>(RpPlayerCivilianIdentityTable)

    override fun toNetwork() = NetworkIdentity.Civilian(
        uuid = player.uuid,
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}