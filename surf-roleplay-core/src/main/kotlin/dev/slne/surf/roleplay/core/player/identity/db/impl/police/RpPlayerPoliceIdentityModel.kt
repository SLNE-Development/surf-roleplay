package dev.slne.surf.roleplay.core.player.identity.db.impl.police

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.player.identity.db.RpPlayerIdentityBaseModel
import dev.slne.surf.roleplay.core.player.identity.identities.PoliceIdentityImpl
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RpPlayerPoliceIdentityModel(
    id: EntityID<Long>
) : RpPlayerIdentityBaseModel<RpIdentity.PoliceIdentity>(
    id,
    RpPlayerPoliceIdentityTable
) {
    companion object : LongEntityClass<RpPlayerPoliceIdentityModel>(RpPlayerPoliceIdentityTable)

    var badgeNumber by RpPlayerPoliceIdentityTable.badgeNumber
    var rank by RpPlayerPoliceIdentityTable.rank

    override suspend fun toApi() = PoliceIdentityImpl(
        player = RpPlayer[player.uuid],
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth,
        badgeNumber = badgeNumber,
        rank = rank,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}