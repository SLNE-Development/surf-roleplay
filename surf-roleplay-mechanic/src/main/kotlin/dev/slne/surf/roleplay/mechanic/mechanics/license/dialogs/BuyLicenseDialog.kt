@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.mechanic.mechanics.license.dialogs

import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import io.papermc.paper.registry.data.dialog.DialogBase

fun buyLicenseDialog(player: LicensePlayer, license: License) = dialog {
    base {
        title { primary("Lizenzen kaufen") }
        afterAction(DialogBase.DialogAfterAction.NONE)

        body {
            plainMessage(400) {
                info("Hier kannst du Lizenzen kaufen.")
            }
            plainMessage(400) {}
            plainMessage(400) {
                info("Wähle eine Lizenz aus, um sie zu kaufen.")
            }
        }
    }

    type {
        multiAction {
            
        }
    }
}