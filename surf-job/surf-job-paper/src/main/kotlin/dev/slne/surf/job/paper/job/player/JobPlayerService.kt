package dev.slne.surf.job.paper.job.player

import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.getJob
import dev.slne.surf.job.api.job.jobs.neutral.CitizenJob
import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import java.util.*

object JobPlayerService {

    private val _players = mutableObjectSetOf<JobPlayer>()
    val players get() = _players.freeze()

    fun remove(uuid: UUID) = _players.removeIf { it.rpPlayer.uuid == uuid }

    suspend operator fun get(uuid: UUID) = _players.firstOrNull { it.rpPlayer.uuid == uuid }
        ?: JobPlayerImpl(RpPlayer[uuid], JobRegistry.getJob<CitizenJob>()).apply {
            _players.add(this)
        }
}