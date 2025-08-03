package dev.slne.surf.roleplay.mechanic.mechanics.license.listeners

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.license.LicenseMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.dialogs.licenseDialog
import dev.slne.surf.roleplay.mechanic.mechanics.license.player.licensePlayer
import dev.slne.surf.roleplay.mechanic.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object LicenseBuyHandler : Listener {

    private val licenseNpc
        get() = surfNpcApi.getNpc(LicenseMechanicImpl.NPC_NAME)
            ?: error("The license NPC is not registered")

    @EventHandler
    fun onPlayerInteractAtEntity(event: NpcInteractEvent) {
        val player = event.player
        val npc = event.npc

        if (npc.npcUuid != licenseNpc.npcUuid) return

        plugin.launch(plugin.entityDispatcher(player)) {
            val rpPlayer = RpPlayer[player.uniqueId]
            val licensePlayer = rpPlayer.licensePlayer()

            player.showDialog(licenseDialog(licensePlayer))
        }
    }

}