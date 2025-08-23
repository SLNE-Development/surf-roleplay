package dev.slne.surf.roleplay.server.player.identity.db

import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongIdTable
import dev.slne.surf.roleplay.server.player.db.RpPlayerTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.date

abstract class RpPlayerIdentityBaseTable(table: String) : AuditableLongIdTable(table) {
    val player = reference(
        "player_id",
        RpPlayerTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    ).uniqueIndex()

    val firstName = char("first_name", 16)
    val lastName = char("last_name", 16)
    val dateOfBirth = date("date_of_birth")
}