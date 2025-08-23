@file:OptIn(InternalRoleplayApi::class)

package dev.slne.surf.roleplay.core.common.player.identity

import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.core.common.player.RpPlayerManager
import dev.slne.surf.roleplay.core.common.player.license.*
import dev.slne.surf.roleplay.core.common.player.license.events.RpPlayerLicenseAddedEvent
import dev.slne.surf.roleplay.core.common.player.license.events.RpPlayerLicenseRemovedEvent
import dev.slne.surf.roleplay.core.common.player.license.utils.LicenseCreateResult
import dev.slne.surf.roleplay.core.common.player.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.core.common.player.license.utils.UnobtainableReason
import dev.slne.surf.roleplay.core.common.transaction.HasRpTransactions
import dev.slne.surf.roleplay.core.common.transaction.RpTransaction
import dev.slne.surf.roleplay.core.common.transaction.utils.BalanceType
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

@Serializable
abstract class RpIdentity(
    open val uuid: @Contextual UUID,
    open val type: RpIdentityType,
    open var firstName: String,
    open var lastName: String,
    open var dateOfBirth: @Contextual LocalDate,
    open val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now(),
    open var updatedAt: @Contextual ZonedDateTime = ZonedDateTime.now()
) : HasRpTransactions, HasLicenses {

    val player: RpPlayer get() = RpPlayer[uuid]
    private val licenseService get() = LicenseService.instance

    @Transient
    private val _licenses = mutableObjectSetOf<IdentityLicense>()
    override val licenses get() = _licenses

    fun addLicense(license: IdentityLicense) {
        _licenses.add(license)
    }

    suspend fun <T : RpIdentity> updateInformation(identity: T.() -> Unit): T {
        TODO("Not yet implemented")
    }

    override suspend fun addLicense(license: License): LicenseCreateResult {
        val (canObtain, reason) = license.canObtain(this)

        if (!canObtain) {
            return LicenseCreateResult(false, reason, null)
        }

        val expiresAt = if (license is ExpirableLicense) {
            ZonedDateTime.now().plusSeconds(license.expiresIn.inWholeSeconds)
        } else {
            null
        }

        val playerLicense = IdentityLicense(
            identity = this,
            license = license,
            expiresAt = expiresAt,
        )

        val event = RpPlayerLicenseAddedEvent(
            source = this,
            player = player,
            identity = this,
            license = playerLicense,
        ).also { it.post() }

        if (event.isCancelled) {
            return LicenseCreateResult(
                false,
                objectSetOf(UnobtainableReason.EventCancelled(event.cancelReason)),
                null
            )
        }

        player.removeCashBalance(license.price)

        val createdPlayerLicense = licenseService.createLicense(playerLicense)
        _licenses.add(createdPlayerLicense)

        return LicenseCreateResult(
            true,
            objectSetOf(),
            createdPlayerLicense
        )
    }

    override suspend fun removeLicense(license: License, reason: LicenseRemovedReason): Boolean {
        val playerLicense = getLicense(license.javaClass) ?: return false

        val event = RpPlayerLicenseRemovedEvent(
            source = this,
            player = player,
            identity = this,
            license = playerLicense,
            reason = reason
        ).also { it.post() }

        if (event.isCancelled) {
            return false
        }

        val result = licenseService.removeLicense(this, license)

        if (result) {
            _licenses.removeIf { it.license == license }
        }

        return result
    }

    override suspend fun confiscateLicense(
        identity: RpIdentity,
        license: License,
        confiscatedBy: RpPlayer,
        confiscatedReason: String
    ) = licenseService.confiscateLicense(identity, license, confiscatedBy, confiscatedReason)

    override fun getLicense(license: Class<out License>) =
        _licenses.firstOrNull { it.license::class.java == license }

    override fun hasLicense(license: Class<out License>) =
        _licenses.any { it.license::class.java == license }

    @Transient
    private val currencyMap = mutableObject2ObjectMapOf(
        BalanceType.CASH to 0,
        BalanceType.BANK to 0,
        BalanceType.CRYPTO to 0
    )

    override suspend fun getBalance(balanceType: BalanceType): Int {
        val currencyName = balanceType.getCurrencyName(this)
            ?: throw IllegalArgumentException("Unknown balance type: $balanceType")

        return currencyMap[balanceType] ?: 0
    }

    override suspend fun addBalance(
        balanceType: BalanceType,
        amount: Int
    ): Boolean {
        val currencyName = balanceType.getCurrencyName(this)
            ?: throw IllegalArgumentException("Unknown balance type: $balanceType")

        return if (amount > 0) {
            currencyMap[balanceType] = (currencyMap[balanceType] ?: 0) + amount
            true
        } else {
            false
        }
    }

    override suspend fun removeBalance(
        balanceType: BalanceType,
        amount: Int
    ): Boolean {
        val currencyName = balanceType.getCurrencyName(this)
            ?: throw IllegalArgumentException("Unknown balance type: $balanceType")

        return if (amount > 0 && (currencyMap[balanceType] ?: 0) >= amount) {
            currencyMap[balanceType] = (currencyMap[balanceType] ?: 0) - amount
            true
        } else {
            false
        }
    }

    override suspend fun getBalanceHistory(
        balanceType: BalanceType,
        limit: Int
    ): ObjectLinkedOpenHashSet<RpTransaction> {
        val currencyName = balanceType.getCurrencyName(this)
            ?: throw IllegalArgumentException("Unknown balance type: $balanceType")

        return ObjectLinkedOpenHashSet()
    }

    override fun toString(): String {
        return "RpIdentity(uuid=$uuid, type=$type, firstName='$firstName', lastName='$lastName', dateOfBirth=$dateOfBirth, createdAt=$createdAt, updatedAt=$updatedAt)"
    }

}