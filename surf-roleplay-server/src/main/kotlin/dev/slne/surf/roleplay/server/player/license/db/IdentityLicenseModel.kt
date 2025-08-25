package dev.slne.surf.roleplay.server.player.license.db

import dev.slne.surf.roleplay.core.common.player.license.NetworkIdentityLicense
import dev.slne.surf.roleplay.server.player.db.RpPlayerModel
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

    fun toNetwork() = NetworkIdentityLicense(
        owner = player.uuid,
        identityType = identity,
        licenseKey = license,
        expiresAt = expiresAt,
        createdAt = createdAt
    )
}