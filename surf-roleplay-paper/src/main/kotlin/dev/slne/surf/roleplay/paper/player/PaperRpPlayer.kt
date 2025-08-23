package dev.slne.surf.roleplay.paper.player

import dev.slne.surf.roleplay.RoleplayApplication
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.core.common.player.getIdentity
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import dev.slne.surf.roleplay.core.common.transaction.utils.BalanceType
import dev.slne.surf.roleplay.paper.player.identity.PaperIdentityService
import dev.slne.surf.roleplay.paper.player.identity.RpIdentity
import dev.slne.surf.roleplay.paper.player.identity.identities.CivilianIdentity
import dev.slne.surf.roleplay.paper.player.license.HasLicenses
import dev.slne.surf.roleplay.paper.player.license.License
import dev.slne.surf.roleplay.paper.player.license.utils.LicenseRemovedReason
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import org.springframework.beans.factory.getBean
import java.util.*

class PaperRpPlayer(uuid: UUID) : RpPlayer(uuid), HasLicenses {

    private val _identities =
        mutableObjectSetOf<RpIdentity>() // TODO: 22.08.2025 20:31 - replace with cloud cache soon
    val identities = _identities.freeze()

    var activeIdentity: RpIdentity? = null
        private set

    override val licenses
        get() = activeIdentity?.licenses
            ?: error("Tried accessing licenses of a player without an active identity $uuid")

    suspend fun fetchIdentities() {
        val fetched = identityService.fetchIdentities(uuid)

        _identities.clear()
        _identities.addAll(fetched)
    }

    fun setActiveIdentity(identity: RpIdentity) {
        require(identity in _identities) { "Tried to set an active identity that is not part of the player's identities: $identity" }
        activeIdentity = identity
    }

    fun addIdentity(identity: RpIdentity) = _identities.add(identity)

    suspend fun <T : RpIdentity> createIdentity(identity: T) =
        identityService.createIdentity(identity)

    suspend fun <T : RpIdentity> updateIdentity(identity: T) =
        identityService.updateIdentity(identity)

    suspend fun <T : RpIdentity> createOrUpdateIdentity(identity: T) =
        identityService.createOrUpdateIdentity(identity)

    fun getIdentity(type: RpIdentityType) = _identities.find { it.type == type }

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

    fun <T : RpIdentity> getIdentity(clazz: Class<out T>) =
        identities.find { clazz.isAssignableFrom(it.javaClass) }

    fun hasCompletedCitizenship() = getIdentity(RpIdentityType.CIVILIAN) != null

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
    ) ?: error("Tried confiscating a license from a player without an active identity $uuid")

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

    companion object {
        private val identityService get() = RoleplayApplication.context.getBean<PaperIdentityService>()
        operator fun get(uuid: UUID) = RpPlayer[uuid] as PaperRpPlayer
    }
}