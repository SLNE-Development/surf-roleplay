package dev.slne.surf.roleplay.server.player.identity.db.impl.civilian

import dev.slne.surf.roleplay.server.player.identity.db.RpPlayerIdentityBaseClass
import dev.slne.surf.roleplay.server.player.identity.db.RpPlayerIdentityBaseModel
import org.jetbrains.exposed.dao.id.EntityID

class RpPlayerCivilianIdentityModel(id: EntityID<Long>) :
    RpPlayerIdentityBaseModel(id, RpPlayerCivilianIdentityTable) {
    companion object :
        RpPlayerIdentityBaseClass<RpPlayerCivilianIdentityModel>(RpPlayerCivilianIdentityTable)
}