package dev.slne.surf.roleplay.core.player.db

import dev.slne.surf.roleplay.core.player.RpPlayerImpl
import dev.slne.surf.roleplay.server.player.identity.db.impl.police.RpPlayerPoliceIdentityModel
import dev.slne.surf.roleplay.server.player.db.RpPlayerTable
import dev.slne.surf.roleplay.server.player.identity.db.impl.civilian.RpPlayerCivilianIdentityModel
import dev.slne.surf.roleplay.server.player.identity.db.impl.civilian.RpPlayerCivilianIdentityTable
import dev.slne.surf.roleplay.server.player.identity.db.impl.police.RpPlayerPoliceIdentityTable
import dev.slne.surf.roleplay.server.player.identity.db.impl.rescueservice.RpPlayerRescueServiceIdentityModel
import dev.slne.surf.roleplay.server.player.identity.db.impl.rescueservice.RpPlayerRescueServiceIdentityTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RpPlayerModel(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<RpPlayerModel>(RpPlayerTable)

    var uuid by RpPlayerTable.uuid
    var username by RpPlayerTable.username

    var createdAt by RpPlayerTable.createdAt
    var updatedAt by RpPlayerTable.updatedAt

    val civilianIdentity by RpPlayerCivilianIdentityModel optionalBackReferencedOn RpPlayerCivilianIdentityTable.player
    val policeIdentity by RpPlayerPoliceIdentityModel optionalBackReferencedOn RpPlayerPoliceIdentityTable.player
    val rescueServiceIdentity by RpPlayerRescueServiceIdentityModel optionalBackReferencedOn RpPlayerRescueServiceIdentityTable.player

    suspend fun toApi() = newSuspendedTransaction(Dispatchers.IO) {
        RpPlayerImpl(uuid = uuid).apply {
            civilianIdentity?.let { addIdentity(it.toApi(this)) }
            policeIdentity?.let { addIdentity(it.toApi(this)) }
            rescueServiceIdentity?.let { addIdentity(it.toApi(this)) }

            this.username = this@RpPlayerModel.username
            this.createdAt = this@RpPlayerModel.createdAt
            this.updatedAt = this@RpPlayerModel.updatedAt
        }
    }
}