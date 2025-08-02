package dev.slne.surf.roleplay.core.player.db

import dev.slne.surf.database.database.columns.CurrentZonedDateTime
import dev.slne.surf.database.database.columns.zonedDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.date

object RpPlayerTable : LongIdTable("rp_players") {

    val uuid = uuid("uuid").uniqueIndex()
    val username = varchar("username", 16).nullable()

    val firstName = varchar("first_name", 16).nullable()
    val lastName = varchar("last_name", 16).nullable()
    val birthDate = date("birth_date").nullable()

    val createdAt = zonedDateTime("created_at").defaultExpression(CurrentZonedDateTime)
    val updatedAt = zonedDateTime("updated_at").defaultExpression(CurrentZonedDateTime)

}