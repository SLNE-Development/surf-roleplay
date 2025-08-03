package dev.slne.surf.roleplay.mechanic.mechanics.jobwages

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.roleplay.api.mechanic.jobwages.event.PlayerPaycheckEvent
import dev.slne.surf.surfapi.core.api.util.logger
import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

class JobWagesJob(
    val delay: Duration,
    val plugin: SuspendingJavaPlugin
) {
    private val map = ConcurrentHashMap<UUID, ZonedDateTime>()
    private val playerDelay = 1.hours
    private val log = logger()

    private val scope =
        CoroutineScope(SupervisorJob() + CoroutineName("JobWagesJob") + CoroutineExceptionHandler { _, throwable ->
            log.atSevere().withCause(throwable).log("JobWagesJob failed")
        })

    fun start() = scope.launch {
        while (isActive) {
            tick()
            delay(delay)
        }
    }

    fun stop() {
        scope.cancel()
    }

    suspend fun tick() {
        JobRegistry.jobs.forEach { job ->
            job.players.forEach { player ->

                val rpPlayer = player.rpPlayer
                val latest = map.get(rpPlayer.uuid)
                val now = ZonedDateTime.now()

                if(latest == null){
                    map.put(rpPlayer.uuid, now)
                    return@forEach
                }

                if (latest.plusSeconds(playerDelay.inWholeSeconds).isAfter(now)) {
                    return@forEach
                }

                val event = PlayerPaycheckEvent(
                    player = rpPlayer,
                    amount = job.income
                )

                if (!event.callEvent()) {
                    return@forEach
                }

                if(event.amount <= 0) {
                    return@forEach
                }

                rpPlayer.addBankBalance(event.amount.toDouble())
                map.put(rpPlayer.uuid, now)
            }
        }
    }
}