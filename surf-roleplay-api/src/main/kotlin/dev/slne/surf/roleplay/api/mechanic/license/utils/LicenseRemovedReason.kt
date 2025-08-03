package dev.slne.surf.roleplay.api.mechanic.license.utils

import dev.slne.surf.roleplay.api.mechanic.license.PlayerLicense
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder

sealed class LicenseRemovedReason(val message: SurfComponentBuilder.(PlayerLicense) -> Unit) {
    object Expired : LicenseRemovedReason({
        info("Deine ")
        append(it.license.displayName)
        info(" Lizenz ist abgelaufen.")
    })

    object Revoked : LicenseRemovedReason({
        info("Deine ")
        append(it.license.displayName)
        info(" Lizenz wurde widerrufen.")
    })

    object Confiscated : LicenseRemovedReason({
        info("Deine ")
        append(it.license.displayName)
        info(" Lizenz wurde beschlagnahmt.")
    })

    class ConfiscatedChild(val parent: PlayerLicense) : LicenseRemovedReason({
        info("Deine ")
        append(it.license.displayName)
        info(" Lizenz wurde beschlagnahmt, da die übergeordnete Lizenz ")
        append(parent.license.displayName)
        info(" beschlagnahmt wurde.")
    })

    object Unknown : LicenseRemovedReason({
        info("Deine ")
        append(it.license.displayName)
        info(" Lizenz wurde entfernt, Grund unbekannt.")
    })
}