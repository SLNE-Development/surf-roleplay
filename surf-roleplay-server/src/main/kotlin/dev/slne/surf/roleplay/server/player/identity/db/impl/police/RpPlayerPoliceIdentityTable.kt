package dev.slne.surf.roleplay.server.player.identity.db.impl.police

import dev.slne.surf.roleplay.server.player.identity.db.RpPlayerIdentityBaseTable

object RpPlayerPoliceIdentityTable : RpPlayerIdentityBaseTable("rp_player_police_identities") {
    val rank = varchar("rank", 255)
    val badgeNumber = varchar("badge_number", 255)
}