package dev.slne.surf.roleplay.server.player.license.db

import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.common.player.license.IdentityLicense
import dev.slne.surf.roleplay.api.player.license.LicenseService
import dev.slne.surf.roleplay.core.player.db.RpPlayerModel
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class IdentityLicenseModel(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<IdentityLicenseModel>(IdentityLicenseTable)

    var player by RpPlayerModel referencedOn IdentityLicenseTable.player
    var identity by IdentityLicenseTable.identity

    var license by IdentityLicenseTable.license
    var expiresAt by IdentityLicenseTable.expiresAt
    var createdAt by IdentityLicenseTable.createdAt

    fun toApi(identity: RpIdentity): IdentityLicense {
        val license = IdentityLicense(
            identity = identity,
            license = LicenseService.getLicenseByKeyOrThrow(key(license)),
            expiresAt = expiresAt,
            createdAt = createdAt
        )

        return license
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IdentityLicenseModel) return false

        if (player != other.player) return false
        if (license != other.license) return false
        if (identity != other.identity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = player.hashCode()
        result = 31 * result + license.hashCode()
        return result
    }
}