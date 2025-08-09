package dev.slne.surf.roleplay.core.common.player.license

import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.common.player.license.ExpirableLicense
import dev.slne.surf.roleplay.api.common.player.license.IdentityLicense
import dev.slne.surf.roleplay.api.common.player.license.License
import dev.slne.surf.roleplay.api.common.player.license.event.RpPlayerLicenseRemovedEvent
import dev.slne.surf.roleplay.api.common.player.license.utils.LicenseCreateResult
import dev.slne.surf.roleplay.api.common.player.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.api.common.transaction.RpTransaction
import dev.slne.surf.roleplay.api.common.transaction.utils.BalanceType
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import java.time.LocalDate
import java.time.ZonedDateTime

abstract class CommonRpIdentity(
    override val player: RpPlayer,
    override val type: RpIdentity.RpIdentityType,
    override var firstName: String,
    override var lastName: String,
    override var dateOfBirth: LocalDate,
    override val createdAt: ZonedDateTime = ZonedDateTime.now(),
    override var updatedAt: ZonedDateTime = ZonedDateTime.now()
) : RpIdentity {

    private val _licenses = mutableObjectSetOf<IdentityLicense>()
    override val licenses get() = _licenses

    fun addLicense(license: IdentityLicense) {
        _licenses.add(license)
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

//        val event = RpPlayerLicenseAddedEvent(
//            player = player,
//            identity = this,
//            license = playerLicense,
//        )
//
//        if (!event.callEvent()) {
//            return LicenseCreateResult(
//                false,
//                objectSetOf(UnobtainableReason.EventCancelled(event.cancelReason)),
//                null
//            )
//        }
        // TODO: 09.08.2025 23:56 Call event

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
        return "RpIdentityImpl(type=$type, firstName='$firstName', lastName='$lastName', dateOfBirth=$dateOfBirth, createdAt=$createdAt, updatedAt=$updatedAt, licenses=$licenses)"
    }

}