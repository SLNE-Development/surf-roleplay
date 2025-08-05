package dev.slne.surf.job.paper.job.listener

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.job.paper.job.JobNpc
import dev.slne.surf.job.paper.job.dialogs.createJobsSelectorDialog
import dev.slne.surf.job.paper.job.player.jobPlayer
import dev.slne.surf.job.paper.plugin
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.roleplay.api.player.rpPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object JobNpcHandler : Listener {
    @EventHandler
    fun onNpcInteract(event: NpcInteractEvent) {
        val npc = event.npc

        if (npc.npcUuid != JobNpc.npc.npcUuid) return

        plugin.launch {
            val rpPlayer = event.player.rpPlayer()
            val jobPlayer = rpPlayer.jobPlayer()

            event.player.showDialog(createJobsSelectorDialog(jobPlayer))
        }
    }
}