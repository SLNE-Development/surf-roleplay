package dev.slne.surf.roleplay.server.player.db

import dev.slne.surf.cloud.api.server.exposed.columns.nativeUuid
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongIdTable

object RpPlayerTable : AuditableLongIdTable("rp_players") {
    val uuid = nativeUuid("uuid").uniqueIndex()
    val username = char("username", 16).nullable()
}