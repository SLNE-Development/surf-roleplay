package dev.slne.surf.roleplay.mechanic.mechanics.license.db

import dev.slne.surf.database.database.columns.CurrentZonedDateTime
import dev.slne.surf.database.database.columns.zonedDateTime
import dev.slne.surf.roleplay.core.player.db.RpPlayerTable
import org.jetbrains.exposed.dao.id.LongIdTable

object PlayerLicenseTable : LongIdTable("player_licenses") {

    val rpPlayer = reference("player_id", RpPlayerTable)
    val license = varchar("license", 255)
    val expiresAt = zonedDateTime("expires_at").nullable()
    val createdAt = zonedDateTime("created_at").defaultExpression(CurrentZonedDateTime)

}