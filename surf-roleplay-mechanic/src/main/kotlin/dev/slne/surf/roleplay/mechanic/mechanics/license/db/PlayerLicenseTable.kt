package dev.slne.surf.roleplay.mechanic.mechanics.license.db

import dev.slne.surf.roleplay.core.player.db.RpPlayerTable
import org.jetbrains.exposed.dao.id.LongIdTable

object PlayerLicenseTable : LongIdTable("mechanic_player_licenses") {

    val rpPlayer = reference("player_id", RpPlayerTable)

}