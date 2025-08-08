package dev.slne.surf.roleplay.core.player.identity.db.impl.rescueservice

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.player.identity.db.RpPlayerIdentityBaseModel
import dev.slne.surf.roleplay.core.player.identity.identities.RescueServiceIdentityImpl
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RpPlayerRescueServiceIdentityModel(
    id: EntityID<Long>
) : RpPlayerIdentityBaseModel<RpIdentity.RescueServiceIdentity>(
    id,
    RpPlayerRescueServiceIdentityTable
) {
    companion object :
        LongEntityClass<RpPlayerRescueServiceIdentityModel>(RpPlayerRescueServiceIdentityTable)

    var rank by RpPlayerRescueServiceIdentityTable.rank

    override suspend fun toApi() = RescueServiceIdentityImpl(
        player = RpPlayer[player.uuid],
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth,
        rank = rank,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}