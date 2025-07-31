package dev.slne.surf.job.paper.player

import com.google.auto.service.AutoService
import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.job.api.player.JobPlayerManager
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import net.kyori.adventure.util.Services
import java.util.*

@AutoService(JobPlayerManager::class)
class JobPlayerManagerImpl : JobPlayerManager, Services.Fallback {

    private val _players = mutableObjectSetOf<JobPlayer>()
    override val players = _players.freeze()

    override fun get(uuid: UUID) = _players.firstOrNull { it.uuid == uuid }
        ?: JobPlayerImpl(uuid).also { _players.add(it) }

    fun remove(uuid: UUID) = _players.removeIf { it.uuid == uuid }
}

val jobPlayerManagerImpl = JobPlayerManager.INSTANCE as JobPlayerManagerImpl