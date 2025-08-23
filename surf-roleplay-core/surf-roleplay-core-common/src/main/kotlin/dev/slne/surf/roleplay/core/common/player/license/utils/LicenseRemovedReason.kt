package dev.slne.surf.roleplay.core.common.player.license.utils

import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.core.common.player.license.IdentityLicense
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder

sealed class LicenseRemovedReason(private val messageBuilder: SurfComponentBuilder.(IdentityLicense) -> Unit) {
    object Expired : LicenseRemovedReason({
        info("Deine ")
        append(it.license.displayName)
        info(" Lizenz ist abgelaufen.")
    })

    object Sold : LicenseRemovedReason({
        info("Deine ")
        append(it.license.displayName)
        info(" Lizenz wurde verkauft.")
    })

    object Revoked : LicenseRemovedReason({
        info("Deine ")
        append(it.license.displayName)
        info(" Lizenz wurde widerrufen.")
    })

    class Confiscated(
        confiscatedBy: RpPlayer,
        confiscatedReason: String
    ) : LicenseRemovedReason({
        info("Deine ")
        append(it.license.displayName)
        info(" Lizenz wurde durch ")
        append(confiscatedBy)
        info(" beschlagnahmt. Grund: ")
        variableValue(confiscatedReason)
        info(".")
    })

    class ConfiscatedChild(
        val parent: IdentityLicense,
        confiscatedBy: RpPlayer,
        confiscatedReason: String
    ) : LicenseRemovedReason({
        info("Deine ")
        append(it.license.displayName)
        info(" Lizenz wurde von ")
        append(confiscatedBy)
        info(" beschlagnahmt, da die übergeordnete Lizenz ")
        append(parent.license.displayName)
        info(" beschlagnahmt wurde. Grund: ")
        variableValue(confiscatedReason)
        info(".")
    })

    object Unknown : LicenseRemovedReason({
        info("Deine ")
        append(it.license.displayName)
        info(" Lizenz wurde entfernt, Grund unbekannt.")
    })

    fun message(license: IdentityLicense) = buildText { this.messageBuilder(license) }
}