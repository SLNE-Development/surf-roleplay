package dev.slne.surf.roleplay.core.player.license.listeners

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.core.player.license.LicenseNpc
import dev.slne.surf.roleplay.core.player.license.dialogs.licenseDialog
import dev.slne.surf.roleplay.core.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object LicenseBuyHandler : Listener {

    private val licenseNpc
        get() = surfNpcApi.getNpc(LicenseNpc.NPC_NAME)
            ?: error("The license NPC is not registered")

    @EventHandler
    fun onPlayerInteractAtEntity(event: NpcInteractEvent) {
        val player = event.player
        val npc = event.npc

        if (npc.npcUuid != licenseNpc.npcUuid) return

        plugin.launch(plugin.entityDispatcher(player)) {
            val rpPlayer = RpPlayer[player.uniqueId]
            val activeIdentity = rpPlayer.activeIdentity ?: run {
                player.sendText {
                    appendPrefix()

                    error("Du musst eine Identität ausgewählt haben, um Lizenzen zu kaufen.")
                }
                return@launch
            }

            player.showDialog(licenseDialog(rpPlayer, activeIdentity))
        }
    }

}