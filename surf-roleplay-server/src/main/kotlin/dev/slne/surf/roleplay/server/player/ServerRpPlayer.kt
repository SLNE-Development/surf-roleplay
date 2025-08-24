package dev.slne.surf.roleplay.server.player

import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.core.common.transaction.RpTransaction
import dev.slne.surf.roleplay.core.common.transaction.utils.BalanceType
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import net.kyori.adventure.text.Component
import java.util.UUID

class ServerRpPlayer(uuid: UUID) : RpPlayer(uuid) {
    override suspend fun getBalance(balanceType: BalanceType): Int {
        TODO("Not yet implemented")
    }

    override suspend fun addBalance(
        balanceType: BalanceType,
        amount: Int
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun removeBalance(
        balanceType: BalanceType,
        amount: Int
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getBalanceHistory(
        balanceType: BalanceType,
        limit: Int
    ): ObjectLinkedOpenHashSet<RpTransaction> {
        TODO("Not yet implemented")
    }

    override fun asComponent(): Component {
        TODO("Not yet implemented")
    }
}