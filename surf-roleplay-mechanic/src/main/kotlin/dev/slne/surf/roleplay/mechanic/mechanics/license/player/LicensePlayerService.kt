package dev.slne.surf.roleplay.mechanic.mechanics.license.player

import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.roleplay.api.player.RpPlayerManager
import dev.slne.surf.roleplay.core.player.rpPlayerManagerImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.db.PlayerLicenseModel
import dev.slne.surf.roleplay.mechanic.mechanics.license.db.PlayerLicenseTable
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

object LicensePlayerService {

    private val _players = mutableObjectSetOf<LicensePlayer>()
    val players get() = _players.freeze()

    fun remove(uuid: UUID) = _players.removeIf { it.rpPlayer.uuid == uuid }

    private suspend fun fetchLicenses(
        licensePlayer: LicensePlayer
    ) = newSuspendedTransaction(Dispatchers.IO) {
        val rpPlayer = rpPlayerManagerImpl.findOrCreate(licensePlayer.rpPlayer.uuid)

        PlayerLicenseModel.find { (PlayerLicenseTable.rpPlayer eq rpPlayer.id) }
            .map { it.toApi() }
            .toObjectSet()
    }

    suspend operator fun get(uuid: UUID) = _players.firstOrNull { it.rpPlayer.uuid == uuid }
        ?: LicensePlayerImpl(RpPlayerManager[uuid]).apply {
            addLicensesInternal(fetchLicenses(this))
            _players.add(this)
        }

}