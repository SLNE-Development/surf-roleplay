package dev.slne.surf.roleplay.core.common.player

import dev.slne.surf.cloud.api.common.player.OfflineCloudPlayer
import dev.slne.surf.cloud.api.common.player.toOfflineCloudPlayer
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.getIdentity
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.common.player.license.License
import dev.slne.surf.roleplay.api.common.player.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.api.common.transaction.utils.BalanceType
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.time.ZonedDateTime
import java.util.*

abstract class CommonRpPlayer(
    private val uuid: UUID,
    override var createdAt: ZonedDateTime = ZonedDateTime.now(),
    override var updatedAt: ZonedDateTime = ZonedDateTime.now()
) : RpPlayer {

    override val cloudPlayer: OfflineCloudPlayer
        get() = uuid.toOfflineCloudPlayer()

    private val _identities = mutableObjectSetOf<RpIdentity>()
    override val identities: ObjectSet<RpIdentity> get() = _identities

    override var activeIdentity: RpIdentity? = null

    override suspend fun setActiveIdentity(identity: RpIdentity) {
        if (!_identities.contains(identity)) {
            error("Tried to set an active identity that is not part of the player's identities: $identity")
        }

        activeIdentity = identity
    }

    fun addIdentity(identity: RpIdentity) = _identities.add(identity)

    override suspend fun <T : RpIdentity> createIdentity(
        identity: T
    ): T {
        TODO("Not yet implemented")
    }

    override suspend fun <T : RpIdentity> updateIdentity(
        identity: T
    ): T? {
        TODO("Not yet implemented")
    }

    override suspend fun <T : RpIdentity> createOrUpdateIdentity(
        identity: T
    ): T {
        TODO("Not yet implemented")
    }

    @Suppress("UNCHECKED_CAST")
    override fun getIdentity(type: RpIdentity.RpIdentityType) =
        _identities.firstOrNull { it.type == type }
            ?: error("No identity of type $type found for player $uuid")

    override suspend fun getBalance(balanceType: BalanceType) =
        activeIdentity?.getBalance(balanceType)
            ?: error("Tried accessing balance of a player without an active identity $uuid")

    override suspend fun addBalance(
        balanceType: BalanceType,
        amount: Int
    ) = activeIdentity?.addBalance(balanceType, amount)
        ?: error("Tried adding balance to a player without an active identity $uuid")

    override suspend fun removeBalance(
        balanceType: BalanceType,
        amount: Int
    ) = activeIdentity?.removeBalance(balanceType, amount)
        ?: error("Tried removing balance from a player without an active identity $uuid")

    override suspend fun getBalanceHistory(
        balanceType: BalanceType,
        limit: Int
    ) = activeIdentity?.getBalanceHistory(balanceType, limit)
        ?: error("Tried accessing balance history of a player without an active identity $uuid")

    override fun <T : RpIdentity> getIdentity(clazz: Class<out T>) = identities.firstOrNull {
        clazz.isAssignableFrom(it.javaClass)
    }

    override fun hasCompletedCitizenship() = getIdentity<RpIdentity.CivilianIdentity>() != null

    override val licenses
        get() = activeIdentity?.licenses
            ?: error("Tried accessing licenses of a player without an active identity $uuid")

    override suspend fun addLicense(license: License) =
        activeIdentity?.addLicense(license)
            ?: error("Tried adding a license to a player without an active identity $uuid")

    override suspend fun removeLicense(
        license: License,
        reason: LicenseRemovedReason
    ) = activeIdentity?.removeLicense(license, reason)
        ?: error("Tried removing a license from a player without an active identity $uuid")

    override fun getLicense(license: Class<out License>) =
        activeIdentity?.getLicense(license)

    override suspend fun confiscateLicense(
        identity: RpIdentity,
        license: License,
        confiscatedBy: RpPlayer,
        confiscatedReason: String
    ) = activeIdentity?.confiscateLicense(
        identity,
        license,
        confiscatedBy,
        confiscatedReason
    )
        ?: error("Tried confiscating a license from a player without an active identity $uuid")

    override suspend fun asComponent() = buildText {
        val activeIdentity = activeIdentity
        val name = cloudPlayer.name()

        if (activeIdentity == null) {
            variableKey(name ?: uuid.toString())

            return@buildText
        }

        val firstName = activeIdentity.firstName
        val lastName = activeIdentity.lastName

        variableValue("$firstName $lastName")

        hoverEvent(buildText {
            variableKey("UUID: ")
            variableValue(uuid.toString())
            appendNewline(2)

            name?.let {
                variableKey("Username: ")
                variableValue(it)
            }
        })
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommonRpPlayer

        return cloudPlayer == other.cloudPlayer
    }

    override fun hashCode(): Int {
        return cloudPlayer.hashCode()
    }

}