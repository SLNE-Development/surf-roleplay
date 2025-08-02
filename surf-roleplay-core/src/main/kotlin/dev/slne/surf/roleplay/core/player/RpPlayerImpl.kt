package dev.slne.surf.roleplay.core.player

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.RpPlayerInformation
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.api.transaction.RpTransaction
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class RpPlayerImpl(
    override val uuid: UUID,
) : RpPlayer {

    override val information: RpPlayerInformation = RpPlayerInformationImpl()
    override var username: String? = null
    override val bukkitPlayer: Player? get() = server.getPlayer(uuid)
    override val bukkitOfflinePlayer: OfflinePlayer get() = server.getOfflinePlayer(uuid)

    override suspend fun getBalance(balanceType: BalanceType): Double {
        TODO("Not yet implemented")
    }

    override suspend fun addBalance(
        balanceType: BalanceType,
        amount: Double
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun removeBalance(
        balanceType: BalanceType,
        amount: Double
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getBalanceHistory(
        balanceType: BalanceType,
        limit: Int
    ): List<RpTransaction> {
        TODO("Not yet implemented")
    }

    override suspend fun hasBalance(
        balanceType: BalanceType,
        amount: Double
    ): Boolean {
        TODO("Not yet implemented")
    }
}