package dev.slne.surf.roleplay.core.player

import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.roleplay.api.mechanic.cash.CashMechanic
import dev.slne.surf.roleplay.api.mechanic.cash.utils.AddCashToInventoryResult
import dev.slne.surf.roleplay.api.mechanic.cash.utils.CashInInventoryResult
import dev.slne.surf.roleplay.api.mechanic.cash.utils.RemoveCashFromInventoryResult
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.RpPlayerInformation
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.api.transaction.RpTransaction
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.util.objectListOf
import it.unimi.dsi.fastutil.objects.ObjectList
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.time.ZonedDateTime
import java.util.*

class RpPlayerImpl(
    override val uuid: UUID,
    override var createdAt: ZonedDateTime = ZonedDateTime.now(),
    override var updatedAt: ZonedDateTime = ZonedDateTime.now()
) : RpPlayer {

    private val balances = mutableMapOf<BalanceType, Double>()

    init {
        balances[BalanceType.BANK] = 100_000.0
        balances[BalanceType.CRYPTO] = 10.0
    }

    override val information: RpPlayerInformation = RpPlayerInformationImpl()
    override var username: String? = null
    override val bukkitPlayer: Player? get() = server.getPlayer(uuid)
    override val bukkitOfflinePlayer: OfflinePlayer get() = server.getOfflinePlayer(uuid)

    override suspend fun updateInformation(update: RpPlayerInformation.() -> Unit) =
        rpPlayerManagerImpl.updatePlayerInformation(this, update)

    override suspend fun updateUsername(username: String) =
        rpPlayerManagerImpl.updateUsername(this, username)

    override suspend fun getBalance(balanceType: BalanceType): Double {
        if (balanceType == BalanceType.CASH) {
            val result = Mechanic.getMechanic<CashMechanic>().getCashBalance(this)

            return if (result is CashInInventoryResult.Success) {
                result.totalAmount.toDouble()
            } else {
                0.0
            }
        }

        return balances.getOrDefault(balanceType, 0.0)
    }

    override suspend fun transferBankBalance(receiver: RpPlayer, amount: Double): Boolean {
        removeBankBalance(amount)
        receiver.addBankBalance(amount)

        return true
    }

    override suspend fun addBalance(
        balanceType: BalanceType,
        amount: Double
    ): Boolean {
        if (balanceType == BalanceType.CASH) {
            return Mechanic.getMechanic<CashMechanic>()
                .addCashBalance(this, amount.toInt()) == AddCashToInventoryResult.SUCCESS
        }

        balances[balanceType] = balances.getOrDefault(balanceType, 0.0) + amount

        return true
    }

    override suspend fun removeBalance(
        balanceType: BalanceType,
        amount: Double
    ): Boolean {
        if (balanceType == BalanceType.CASH) {
            return Mechanic.getMechanic<CashMechanic>()
                .removeCashBalance(this, amount.toInt()) == RemoveCashFromInventoryResult.SUCCESS
        }

        balances[balanceType] = balances.getOrDefault(balanceType, 0.0) - amount

        return true
    }

    override suspend fun getBalanceHistory(
        balanceType: BalanceType,
        limit: Int
    ): ObjectList<RpTransaction> {
        return objectListOf()
    }

    override fun isCitizen() = information.firstName != null &&
            information.lastName != null &&
            information.birthDate != null
}