package dev.slne.surf.roleplay.core.player.license.db

import dev.slne.surf.database.database.columns.CurrentZonedDateTime
import dev.slne.surf.database.database.columns.zonedDateTime
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.player.db.RpPlayerTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object IdentityLicenseTable : LongIdTable("identity_licenses") {

    val player = reference(
        "player_id", RpPlayerTable,
        onUpdate = ReferenceOption.CASCADE,
        onDelete = ReferenceOption.CASCADE
    )
    val identity = enumerationByName<RpIdentity.RpIdentityType>("identity", 255)

    val license = varchar("license", 255)

    val expiresAt = zonedDateTime("expires_at").nullable()
    val createdAt = zonedDateTime("created_at").defaultExpression(CurrentZonedDateTime)

}