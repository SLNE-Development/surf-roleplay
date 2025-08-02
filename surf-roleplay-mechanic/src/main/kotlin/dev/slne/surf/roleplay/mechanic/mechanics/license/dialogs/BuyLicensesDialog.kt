@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.mechanic.mechanics.license.dialogs

import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.roleplay.api.mechanic.license.LicenseMechanic
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import io.papermc.paper.registry.data.dialog.DialogBase

fun buyLicensesDialog() = dialog {
    base {
        title { primary("Lizenzen erwerben") }
        afterAction(DialogBase.DialogAfterAction.NONE)

        body {
            plainMessage(400) {
                info("Hier kannst du Lizenzen erwerben.")
            }
            plainMessage(400) {}
            plainMessage(400) {
                info("Wähle eine Lizenz aus, um sie zu kaufen.")
            }
            plainMessage(400) {}
            plainMessage(400) {
                info("Klicke auf die Schaltfläche 'Lizenz erwerben', um den Kauf abzuschließen.")
            }
        }
    }
}

private fun buildDialogList(
    licensePlayer: LicensePlayer
) = Mechanic.getMechanic<LicenseMechanic>().licenses.map {

}