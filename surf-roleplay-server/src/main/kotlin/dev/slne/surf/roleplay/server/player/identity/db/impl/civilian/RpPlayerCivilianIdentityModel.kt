package dev.slne.surf.roleplay.server.player.identity.db.impl.civilian

import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.identity.identities.CivilianIdentity
import dev.slne.surf.roleplay.server.player.identity.db.RpPlayerIdentityBaseModel
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RpPlayerCivilianIdentityModel(
    id: EntityID<Long>
) : RpPlayerIdentityBaseModel<RpIdentity.CivilianIdentity>(
    id,
    RpPlayerCivilianIdentityTable
) {
    companion object : LongEntityClass<RpPlayerCivilianIdentityModel>(RpPlayerCivilianIdentityTable)

    override suspend fun toApi(player: RpPlayer) = CivilianIdentity(
        player = player,
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth,
        createdAt = createdAt,
        updatedAt = updatedAt,
    ).apply {
//        licenseServiceImpl.fetchLicenses(this).forEach {
//            addLicense(it)
//        }
        // TODO: 09.08.2025 23:53 Fetch licenses and attach to player
    }
}