package dev.slne.surf.roleplay.server.player.identity.db.impl.rescueservice

import dev.slne.surf.roleplay.core.common.player.identity.NetworkIdentity
import dev.slne.surf.roleplay.server.player.identity.db.RpPlayerIdentityBaseClass
import dev.slne.surf.roleplay.server.player.identity.db.RpPlayerIdentityBaseModel
import org.jetbrains.exposed.dao.id.EntityID

class RpPlayerRescueServiceIdentityModel(id: EntityID<Long>) :
    RpPlayerIdentityBaseModel(id, RpPlayerRescueServiceIdentityTable) {
    companion object :
        RpPlayerIdentityBaseClass<RpPlayerRescueServiceIdentityModel>(
            RpPlayerRescueServiceIdentityTable
        )

    var rank by RpPlayerRescueServiceIdentityTable.rank

    override fun toNetwork() = NetworkIdentity.RescueService(
        uuid = player.uuid,
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth,
        rank = rank,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}