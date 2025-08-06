package dev.slne.surf.roleplay.core.player.identity

import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.api.transaction.RpTransaction
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import java.time.LocalDate
import java.time.ZonedDateTime

abstract class RpIdentityImpl(
    override var firstName: String,
    override var lastName: String,
    override var dateOfBirth: LocalDate,
    override val createdAt: ZonedDateTime = ZonedDateTime.now(),
    override var updatedAt: ZonedDateTime = ZonedDateTime.now()
) : RpIdentity {

    private val currencyMap by lazy {
        getCurrencyNames()
    }

    override suspend fun getBalance(balanceType: BalanceType): Double {
        val currencyName = currencyMap[balanceType]
            ?: throw IllegalArgumentException("Unknown balance type: $balanceType")

        return 0.0
    }

    override suspend fun addBalance(
        balanceType: BalanceType,
        amount: Double
    ): Boolean {
        val currencyName = currencyMap[balanceType]
            ?: throw IllegalArgumentException("Unknown balance type: $balanceType")

        return true
    }

    override suspend fun removeBalance(
        balanceType: BalanceType,
        amount: Double
    ): Boolean {
        val currencyName = currencyMap[balanceType]
            ?: throw IllegalArgumentException("Unknown balance type: $balanceType")

        return true
    }

    override suspend fun getBalanceHistory(
        balanceType: BalanceType,
        limit: Int
    ): ObjectLinkedOpenHashSet<RpTransaction> {
        val currencyName = currencyMap[balanceType]
            ?: throw IllegalArgumentException("Unknown balance type: $balanceType")

        return ObjectLinkedOpenHashSet()
    }

    /**
     * Returns a map of currency names for each balance type.
     *
     * @return a map where keys are [BalanceType] and values are the corresponding currency names.
     */
    abstract fun getCurrencyNames(): Map<BalanceType, String>
}