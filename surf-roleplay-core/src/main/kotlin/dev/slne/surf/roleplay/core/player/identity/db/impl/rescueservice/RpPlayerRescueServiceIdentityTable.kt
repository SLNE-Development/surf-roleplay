package dev.slne.surf.roleplay.core.player.identity.db.impl.rescueservice

import dev.slne.surf.roleplay.core.player.identity.db.RpPlayerIdentityBaseTable

object RpPlayerRescueServiceIdentityTable : RpPlayerIdentityBaseTable(
    "rp_player_rescue_service_identities"
) {
    val rank = varchar("rank", 255)
}