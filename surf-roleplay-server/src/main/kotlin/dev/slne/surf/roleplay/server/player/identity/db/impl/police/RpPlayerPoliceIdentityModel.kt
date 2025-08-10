package dev.slne.surf.roleplay.core.player.identity.db.impl.police

import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.identity.identities.PoliceIdentityImpl
import dev.slne.surf.roleplay.server.player.identity.db.RpPlayerIdentityBaseModel
import dev.slne.surf.roleplay.server.player.identity.db.impl.police.RpPlayerPoliceIdentityTable
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

    override suspend fun toApi(player: RpPlayer) = PoliceIdentityImpl(
        player = player,
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth,
        badgeNumber = badgeNumber,
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