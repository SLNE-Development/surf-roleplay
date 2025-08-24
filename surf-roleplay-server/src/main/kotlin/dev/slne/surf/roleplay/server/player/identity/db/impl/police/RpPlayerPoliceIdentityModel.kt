package dev.slne.surf.roleplay.server.player.identity.db.impl.police

import dev.slne.surf.roleplay.core.common.player.identity.NetworkIdentity
import dev.slne.surf.roleplay.server.player.identity.db.RpPlayerIdentityBaseClass
import dev.slne.surf.roleplay.server.player.identity.db.RpPlayerIdentityBaseModel
import org.jetbrains.exposed.dao.id.EntityID

class RpPlayerPoliceIdentityModel(id: EntityID<Long>) :
    RpPlayerIdentityBaseModel(id, RpPlayerPoliceIdentityTable) {
    companion object :
        RpPlayerIdentityBaseClass<RpPlayerPoliceIdentityModel>(RpPlayerPoliceIdentityTable)

    var badgeNumber by RpPlayerPoliceIdentityTable.badgeNumber
    var rank by RpPlayerPoliceIdentityTable.rank

    override fun toNetwork() = NetworkIdentity.Police(
        uuid = player.uuid,
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth,
        badgeNumber = badgeNumber,
        rank = rank,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}