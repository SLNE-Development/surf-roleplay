package dev.slne.surf.roleplay.mechanic.mechanics.license.db

import dev.slne.surf.roleplay.api.mechanic.license.LicenseMechanic
import dev.slne.surf.roleplay.api.mechanic.license.PlayerLicense
import dev.slne.surf.roleplay.api.player.RpPlayerManager
import dev.slne.surf.roleplay.core.player.db.RpPlayerModel
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PlayerLicenseModel(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<PlayerLicenseModel>(PlayerLicenseTable)

    var rpPlayer by RpPlayerModel referencedOn PlayerLicenseTable.rpPlayer
    var license by PlayerLicenseTable.license
    var expiresAt by PlayerLicenseTable.expiresAt
    var createdAt by PlayerLicenseTable.createdAt

    suspend fun toApi() = PlayerLicense(
        player = RpPlayerManager[rpPlayer.uuid],
        license = LicenseMechanic.getLicenseByKey(key(license)),
        expiresAt = expiresAt,
        createdAt = createdAt
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlayerLicenseModel) return false

        if (rpPlayer != other.rpPlayer) return false
        if (license != other.license) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rpPlayer.hashCode()
        result = 31 * result + license.hashCode()
        return result
    }
}