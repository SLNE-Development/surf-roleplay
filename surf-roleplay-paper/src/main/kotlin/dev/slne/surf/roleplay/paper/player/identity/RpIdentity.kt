package dev.slne.surf.roleplay.paper.player.identity

import dev.slne.surf.roleplay.RoleplayApplication
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.core.common.player.identity.NetworkIdentity
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import dev.slne.surf.roleplay.core.common.transaction.HasRpTransactions
import dev.slne.surf.roleplay.core.common.transaction.RpTransaction
import dev.slne.surf.roleplay.core.common.transaction.utils.BalanceType
import dev.slne.surf.roleplay.paper.player.PaperRpPlayer
import dev.slne.surf.roleplay.paper.player.identity.identities.CivilianIdentity
import dev.slne.surf.roleplay.paper.player.identity.identities.PoliceIdentity
import dev.slne.surf.roleplay.paper.player.identity.identities.RescueServiceIdentity
import dev.slne.surf.roleplay.paper.player.license.*
import dev.slne.surf.roleplay.paper.player.license.events.RpPlayerLicenseAddedEvent
import dev.slne.surf.roleplay.paper.player.license.events.RpPlayerLicenseRemovedEvent
import dev.slne.surf.roleplay.paper.player.license.utils.LicenseCreateResult
import dev.slne.surf.roleplay.paper.player.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.paper.player.license.utils.UnobtainableReason
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import kotlinx.serialization.Contextual
import kotlinx.serialization.Transient
import org.springframework.beans.factory.getBean
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

abstract class RpIdentity : HasRpTransactions, HasLicenses {
    abstract val uuid: @Contextual UUID
    abstract val type: RpIdentityType
    abstract var firstName: String
    abstract var lastName: String
    abstract var dateOfBirth: @Contextual LocalDate
    open val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now()
    open var updatedAt: @Contextual ZonedDateTime = ZonedDateTime.now()

    val player get() = PaperRpPlayer[uuid]

    @Transient
    private val _licenses = mutableObjectSetOf<IdentityLicense>()

    @Transient
    override val licenses = _licenses.freeze()

    abstract fun toNetwork(): NetworkIdentity

    fun addLicense(license: IdentityLicense) {
        _licenses.add(license)
    }

    suspend fun <T : RpIdentity> updateInformation(identity: T.() -> Unit): T {
        TODO("Not yet implemented")
    }

    override suspend fun addLicense(license: License): LicenseCreateResult {
        val unobtainableReasons = license.canObtain(this)

        if (unobtainableReasons.isNotEmpty()) {
            return LicenseCreateResult(false, unobtainableReasons, null)
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
        val currencyName = balanceType.getCurrencyName(type)
            ?: throw IllegalArgumentException("Unknown balance type: $balanceType")

        return currencyMap[balanceType] ?: 0
    }

    override suspend fun addBalance(
        balanceType: BalanceType,
        amount: Int
    ): Boolean {
        val currencyName = balanceType.getCurrencyName(type)
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
        val currencyName = balanceType.getCurrencyName(type)
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
        val currencyName = balanceType.getCurrencyName(type)
            ?: throw IllegalArgumentException("Unknown balance type: $balanceType")

        return ObjectLinkedOpenHashSet()
    }

    override fun toString(): String {
        return "RpIdentity(uuid=$uuid, type=$type, firstName='$firstName', lastName='$lastName', dateOfBirth=$dateOfBirth, createdAt=$createdAt, updatedAt=$updatedAt)"
    }

    companion object {
        private val licenseService get() = RoleplayApplication.context.getBean<PaperLicenseService>()


        fun fromNetwork(network: NetworkIdentity) = when (network) {
            is NetworkIdentity.Civilian -> CivilianIdentity(
                network.uuid,
                network.firstName,
                network.lastName,
                network.dateOfBirth,
                network.createdAt,
                network.updatedAt
            )

            is NetworkIdentity.Police -> PoliceIdentity(
                network.uuid,
                network.firstName,
                network.lastName,
                network.dateOfBirth,
                network.badgeNumber,
                network.rank,
                network.createdAt,
                network.updatedAt
            )

            is NetworkIdentity.RescueService -> RescueServiceIdentity(
                network.uuid,
                network.firstName,
                network.lastName,
                network.dateOfBirth,
                network.rank,
                network.createdAt,
                network.updatedAt
            )
        }
    }
}