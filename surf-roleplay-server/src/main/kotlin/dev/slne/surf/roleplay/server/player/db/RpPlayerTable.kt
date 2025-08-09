package dev.slne.surf.roleplay.server.player.db

import dev.slne.surf.cloud.api.server.exposed.columns.CurrentZonedDateTime
import dev.slne.surf.cloud.api.server.exposed.columns.zonedDateTime
import org.jetbrains.exposed.dao.id.LongIdTable

object RpPlayerTable : LongIdTable("rp_players") {

    val uuid = uuid("uuid").uniqueIndex()
    val username = varchar("username", 16).nullable()

    val createdAt = zonedDateTime("created_at").defaultExpression(CurrentZonedDateTime)
    val updatedAt = zonedDateTime("updated_at").defaultExpression(CurrentZonedDateTime)

}