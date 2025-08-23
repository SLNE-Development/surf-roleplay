package dev.slne.surf.roleplay.core.common.player

import dev.slne.surf.cloud.api.common.player.OfflineCloudPlayer
import dev.slne.surf.cloud.api.common.player.toOfflineCloudPlayer
import dev.slne.surf.roleplay.RoleplayApplication
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import dev.slne.surf.roleplay.core.common.player.identity.identities.CivilianIdentity
import dev.slne.surf.roleplay.core.common.player.license.HasLicenses
import dev.slne.surf.roleplay.core.common.player.license.License
import dev.slne.surf.roleplay.core.common.player.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.core.common.transaction.HasRpTransactions
import dev.slne.surf.roleplay.core.common.transaction.utils.BalanceType
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import net.kyori.adventure.text.ComponentLike
import org.springframework.beans.factory.getBean
import java.time.ZonedDateTime
import java.util.*

abstract class RpPlayer(
    private val uuid: UUID,
    var createdAt: ZonedDateTime = ZonedDateTime.now(),
    var updatedAt: ZonedDateTime = ZonedDateTime.now()
) : HasRpTransactions, HasLicenses, ComponentLike {

    val cloudPlayer: OfflineCloudPlayer
        get() = uuid.toOfflineCloudPlayer()

    private val _identities = mutableObjectSetOf<RpIdentity>() // TODO: 22.08.2025 20:31 - replace with cloud cache soon
    val identities = _identities.freeze()

    var activeIdentity: RpIdentity? = null

    private val playerService get() = RoleplayApplication.context.getBean<RpPlayerManager>()

    suspend fun fetchIdentities() {
        val fetched = playerService.fetchIdentities(uuid)

        _identities.clear()
        _identities.addAll(fetched)
    }

    fun setActiveIdentity(identity: RpIdentity) {
        if (!_identities.contains(identity)) {
            error("Tried to set an active identity that is not part of the player's identities: $identity")
        }

        activeIdentity = identity
    }

    fun addIdentity(identity: RpIdentity) = _identities.add(identity)

    suspend fun <T : RpIdentity> createIdentity(identity: T) =
        playerService.createIdentity(identity)

    suspend fun <T : RpIdentity> updateIdentity(identity: T) =
        playerService.updateIdentity(identity)

    suspend fun <T : RpIdentity> createOrUpdateIdentity(identity: T) =
        playerService.createOrUpdateIdentity(identity)

    @Suppress("UNCHECKED_CAST")
    fun getIdentity(type: RpIdentityType) =
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

    fun <T : RpIdentity> getIdentity(clazz: Class<out T>) = identities.firstOrNull {
        clazz.isAssignableFrom(it.javaClass)
    }

    fun hasCompletedCitizenship() = getIdentity<CivilianIdentity>() != null

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

    override fun asComponent() = buildText {
        val activeIdentity = activeIdentity
        val name = cloudPlayer.player?.name

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
        if (other !is RpPlayer) return false

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }


    companion object {
        operator fun get(cloudPlayer: OfflineCloudPlayer) = get(cloudPlayer.uuid)
        operator fun get(uuid: UUID) =
            RoleplayApplication.context.getBean<RpPlayerManager>()
                .getPlayerByUuid(uuid)
    }

}

/**
 * Gets the [RpIdentity] of the specified type for this [RpPlayer].
 *
 * @param T The type of the identity to retrieve.
 * @return The identity of the specified type, or `null` if not found.
 */
inline fun <reified T : RpIdentity> RpPlayer.getIdentity() = getIdentity(T::class.java)

/**
 * Gets the [RpPlayer] associated with this [OfflineCloudPlayer].
 *
 * @return The [RpPlayer] instance for this player.
 */
fun OfflineCloudPlayer.rpPlayer() = RpPlayer[this]