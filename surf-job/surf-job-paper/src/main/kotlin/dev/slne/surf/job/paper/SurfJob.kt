package dev.slne.surf.job.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.job.paper.job.jobRegistryImpl
import dev.slne.surf.job.paper.job.player.JobPlayerHandler
import dev.slne.surf.job.paper.job.player.jobPlayer
import dev.slne.surf.job.paper.utils.PermissionRegistry
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

val plugin get() = SurfJob.plugin

object SurfJob {

    lateinit var plugin: SuspendingJavaPlugin
        private set

    fun onLoad(plugin: SuspendingJavaPlugin) {
        this.plugin = plugin

        jobRegistryImpl.registerJobs()
    }

    fun onEnable() {
        PermissionRegistry.createJobJoinPermissions()
        JobPlayerHandler.register()

        commandAPICommand("current-job") {
            playerExecutor { player, arguments ->
                plugin.launch {
                    val rpPlayer = RpPlayer[player.uniqueId]
                    val jobPlayer = rpPlayer.jobPlayer()

                    player.sendText {
                        appendPrefix()

                        info("Dein aktueller Job: ")
                        append(jobPlayer.currentJob.displayName)
                    }
                }
            }
        }
    }

    fun onDisable() {

    }

}