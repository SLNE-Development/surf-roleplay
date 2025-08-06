package dev.slne.surf.roleplay.mechanic.mechanics.jobwages

import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.roleplay.api.coroutine.RpJob
import dev.slne.surf.roleplay.api.mechanic.jobwages.event.PlayerPaycheckEvent
import dev.slne.surf.roleplay.api.player.RpPlayer
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

object JobWagesJob : RpJob("JobWagesJob", 1.seconds) {

    private val map = ConcurrentHashMap<UUID, Long>()
    private val playerDelay = 1.hours

    fun playerDisconnect(rpPlayer: RpPlayer) {
        map.remove(rpPlayer.uuid)
    }

    override suspend fun tick() {
        JobRegistry.jobs.forEach { job ->
            job.players.forEach { player ->
                val rpPlayer = player.rpPlayer
                val current = map.getOrDefault(rpPlayer.uuid, 0)

                if (current == playerDelay.inWholeSeconds) {
                    val event = PlayerPaycheckEvent(
                        player = rpPlayer,
                        amount = job.income
                    )

                    if (event.callEvent()) {
                        if (event.amount > 0) {
                            rpPlayer.addBankBalance(event.amount.toDouble())
                        }
                    }

                    map[rpPlayer.uuid] = 0
                    return@forEach
                }

                map[rpPlayer.uuid] = current + 1
            }
        }
    }
}