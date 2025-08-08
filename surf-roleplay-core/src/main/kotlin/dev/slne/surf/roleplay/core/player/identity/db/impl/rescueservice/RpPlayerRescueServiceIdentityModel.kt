package dev.slne.surf.roleplay.core.player.identity.db.impl.rescueservice

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.player.identity.db.RpPlayerIdentityBaseModel
import dev.slne.surf.roleplay.core.player.identity.identities.RescueServiceIdentityImpl
import dev.slne.surf.roleplay.core.player.license.licenseServiceImpl
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
        licenseServiceImpl.fetchLicenses(this).forEach {
            addLicense(it)
        }
    }
}