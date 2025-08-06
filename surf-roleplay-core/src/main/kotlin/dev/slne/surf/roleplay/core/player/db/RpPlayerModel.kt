package dev.slne.surf.roleplay.core.player.db

import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.player.RpPlayerImpl
import dev.slne.surf.roleplay.core.player.identity.db.impl.civilian.RpPlayerCivilianIdentityModel
import dev.slne.surf.roleplay.core.player.identity.db.impl.civilian.RpPlayerCivilianIdentityTable
import dev.slne.surf.roleplay.core.player.identity.db.impl.police.RpPlayerPoliceIdentityModel
import dev.slne.surf.roleplay.core.player.identity.db.impl.police.RpPlayerPoliceIdentityTable
import dev.slne.surf.roleplay.core.player.identity.db.impl.rescueservice.RpPlayerRescueServiceIdentityModel
import dev.slne.surf.roleplay.core.player.identity.db.impl.rescueservice.RpPlayerRescueServiceIdentityTable
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
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

    val civilianIdentity by RpPlayerCivilianIdentityModel optionalBackReferencedOn RpPlayerCivilianIdentityTable.rpPlayer
    val policeIdentity by RpPlayerPoliceIdentityModel optionalBackReferencedOn RpPlayerPoliceIdentityTable.rpPlayer
    val rescueServiceIdentity by RpPlayerRescueServiceIdentityModel optionalBackReferencedOn RpPlayerRescueServiceIdentityTable.rpPlayer

    suspend fun toApi() = newSuspendedTransaction(Dispatchers.IO) {
        RpPlayerImpl(
            uuid = uuid,
            identities = mutableObjectSetOf<RpIdentity>().apply {
                civilianIdentity?.let { add(it.toApi()) }
                policeIdentity?.let { add(it.toApi()) }
                rescueServiceIdentity?.let { add(it.toApi()) }
            },
        ).apply {
            this.username = this@RpPlayerModel.username
            this.createdAt = this@RpPlayerModel.createdAt
            this.updatedAt = this@RpPlayerModel.updatedAt
        }
    }
}