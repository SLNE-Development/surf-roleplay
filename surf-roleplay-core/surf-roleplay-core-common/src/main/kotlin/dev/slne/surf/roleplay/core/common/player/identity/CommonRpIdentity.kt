@file:OptIn(InternalRoleplayApi::class)

package dev.slne.surf.roleplay.core.common.player.identity

import dev.slne.surf.cloud.api.common.netty.network.codec.StreamCodec
import dev.slne.surf.cloud.api.common.netty.protocol.buffer.SurfByteBuf
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.RpPlayerManager
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.common.player.license.ExpirableLicense
import dev.slne.surf.roleplay.api.common.player.license.IdentityLicense
import dev.slne.surf.roleplay.api.common.player.license.InternalLicenseBridge
import dev.slne.surf.roleplay.api.common.player.license.License
import dev.slne.surf.roleplay.api.common.player.license.events.RpPlayerLicenseAddedEvent
import dev.slne.surf.roleplay.api.common.player.license.events.RpPlayerLicenseRemovedEvent
import dev.slne.surf.roleplay.api.common.player.license.utils.LicenseCreateResult
import dev.slne.surf.roleplay.api.common.player.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.api.common.player.license.utils.UnobtainableReason
import dev.slne.surf.roleplay.api.common.transaction.RpTransaction
import dev.slne.surf.roleplay.api.common.transaction.utils.BalanceType
import dev.slne.surf.roleplay.api.common.util.InternalRoleplayApi
import dev.slne.surf.roleplay.core.common.player.identity.identities.CivilianIdentityImpl
import dev.slne.surf.roleplay.core.common.player.identity.identities.PoliceIdentityImpl
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.ZonedDateTime

@Serializable
abstract class CommonRpIdentity(
    val codecType: RpIdentityCodecType,
) : RpIdentity {

    override val player: RpPlayer get() = RpPlayerManager.instance.getPlayerByUuid(uuid)
    private val licenseService get() = InternalLicenseBridge.instance

    @Transient
    private val _licenses = mutableObjectSetOf<IdentityLicense>()
    override val licenses get() = _licenses

    fun addLicense(license: IdentityLicense) {
        _licenses.add(license)
    }

    enum class RpIdentityCodecType(val codec: StreamCodec<SurfByteBuf, out CommonRpIdentity>) {
        CIVILIAN(SurfByteBuf.streamCodecFromKotlin(CivilianIdentityImpl.serializer())),
        POLICE(SurfByteBuf.streamCodecFromKotlin(PoliceIdentityImpl.serializer())),
        RESCUE_SERVICE(SurfByteBuf.streamCodecFromKotlin(PoliceIdentityImpl.serializer())),
    }

    override suspend fun <T : RpIdentity> updateInformation(identity: T.() -> Unit): T {
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

}