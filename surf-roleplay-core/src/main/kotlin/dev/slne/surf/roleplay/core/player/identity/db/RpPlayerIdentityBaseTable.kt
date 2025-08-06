package dev.slne.surf.roleplay.core.player.identity.db

import dev.slne.surf.database.database.columns.CurrentZonedDateTime
import dev.slne.surf.database.database.columns.zonedDateTime
import dev.slne.surf.roleplay.core.player.db.RpPlayerTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.date

abstract class RpPlayerIdentityBaseTable(table: String) : LongIdTable(table) {
    val rpPlayer = reference(
        "player_id",
        RpPlayerTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    ).uniqueIndex()

    val firstName = varchar("first_name", 16)
    val lastName = varchar("last_name", 16)
    val dateOfBirth = date("date_of_birth")

    val createdAt = zonedDateTime("created_at").defaultExpression(CurrentZonedDateTime)
    val updatedAt = zonedDateTime("updated_at").defaultExpression(CurrentZonedDateTime)
}