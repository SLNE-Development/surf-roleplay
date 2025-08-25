package dev.slne.surf.roleplay.server.player.license.db

import dev.slne.surf.cloud.api.server.exposed.columns.CurrentZonedDateTime
import dev.slne.surf.cloud.api.server.exposed.columns.zonedDateTime
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import dev.slne.surf.roleplay.server.player.db.RpPlayerTable
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object IdentityLicenseTable : LongIdTable("identity_licenses") {

    val player = reference(
        "player_id", RpPlayerTable,
        onUpdate = ReferenceOption.CASCADE,
        onDelete = ReferenceOption.CASCADE
    )
    val identity = enumerationByName<RpIdentityType>("identity", 255)
    val license = char("license", 255).transform({ key(it) }, { it.asString() })

    val expiresAt = zonedDateTime("expires_at").nullable()
    val createdAt = zonedDateTime("created_at").defaultExpression(CurrentZonedDateTime)

}