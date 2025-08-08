package dev.slne.surf.roleplay.core.player.identity

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.player.license.ExpirableLicense
import dev.slne.surf.roleplay.api.player.license.IdentityLicense
import dev.slne.surf.roleplay.api.player.license.License
import dev.slne.surf.roleplay.api.player.license.LicenseService
import dev.slne.surf.roleplay.api.player.license.event.RpPlayerLicenseAddedEvent
import dev.slne.surf.roleplay.api.player.license.event.RpPlayerLicenseRemovedEvent
import dev.slne.surf.roleplay.api.player.license.utils.LicenseCreateResult
import dev.slne.surf.roleplay.api.player.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.api.player.license.utils.UnobtainableReason
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.api.transaction.RpTransaction
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import java.time.LocalDate
import java.time.ZonedDateTime

abstract class RpIdentityImpl(
    override val player: RpPlayer,
    override val type: RpIdentity.RpIdentityType,
    override var firstName: String,
    override var lastName: String,
    override var dateOfBirth: LocalDate,
    override val createdAt: ZonedDateTime = ZonedDateTime.now(),
    override var updatedAt: ZonedDateTime = ZonedDateTime.now()
) : RpIdentity {

    private val currencyMap by lazy {
        getCurrencyNames()
    }

    private val _licenses = mutableObjectSetOf<IdentityLicense>()
    override val licenses get() = _licenses

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
            player = player,
            identity = this,
            license = playerLicense,
        )

        if (!event.callEvent()) {
            return LicenseCreateResult(
                false,
                objectSetOf(UnobtainableReason.EventCancelled(event.cancelReason)),
                null
            )
        }

        player.removeCashBalance(license.price)

        val createdPlayerLicense = LicenseService.createLicense(playerLicense)
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
            player = player,
            identity = this,
            license = playerLicense,
            reason = reason
        )

        if (!event.callEvent()) {
            return false
        }

        val result = LicenseService.removeLicense(this, license)

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
    ) = LicenseService.confiscateLicense(identity, license, confiscatedBy, confiscatedReason)

    override fun getLicense(license: Class<out License>) =
        _licenses.firstOrNull { it.license::class.java == license }

    override fun hasLicense(license: Class<out License>) =
        _licenses.any { it.license::class.java == license }

    override suspend fun getBalance(balanceType: BalanceType): Int {
        val currencyName = currencyMap[balanceType]
            ?: throw IllegalArgumentException("Unknown balance type: $balanceType")

        return 0
    }

    override suspend fun addBalance(
        balanceType: BalanceType,
        amount: Int
    ): Boolean {
        val currencyName = currencyMap[balanceType]
            ?: throw IllegalArgumentException("Unknown balance type: $balanceType")

        return true
    }

    override suspend fun removeBalance(
        balanceType: BalanceType,
        amount: Int
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
    
    override fun toString(): String {
        return "RpIdentityImpl(player=$player, type=$type, firstName='$firstName', lastName='$lastName', dateOfBirth=$dateOfBirth, createdAt=$createdAt, updatedAt=$updatedAt, licenses=$licenses)"
    }

}