package dev.slne.surf.roleplay.core.player.identity.db.impl.civilian

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.player.identity.db.RpPlayerIdentityBaseModel
import dev.slne.surf.roleplay.core.player.identity.identities.CivilianIdentityImpl
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RpPlayerCivilianIdentityModel(
    id: EntityID<Long>
) : RpPlayerIdentityBaseModel<RpIdentity.CivilianIdentity>(
    id,
    RpPlayerCivilianIdentityTable
) {
    companion object : LongEntityClass<RpPlayerCivilianIdentityModel>(RpPlayerCivilianIdentityTable)

    override suspend fun toApi() = CivilianIdentityImpl(
        player = RpPlayer[player.uuid],
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}