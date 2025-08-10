package dev.slne.surf.roleplay.server.player.identity.db.impl.rescueservice

import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.identity.identities.RescueServiceIdentityImpl
import dev.slne.surf.roleplay.server.player.identity.db.RpPlayerIdentityBaseModel
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

    override suspend fun toApi(player: RpPlayer) = RescueServiceIdentityImpl(
        player = player,
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth,
        rank = rank,
        createdAt = createdAt,
        updatedAt = updatedAt,
    ).apply {
//        licenseServiceImpl.fetchLicenses(this).forEach {
//            addLicense(it)
//        }
        // TODO: 09.08.2025 23:53 Fetch licenses and attach to player
    }
}