package dev.slne.surf.roleplay.api.common.util

import dev.slne.surf.surfapi.core.api.util.logger
import kotlinx.coroutines.*
import kotlin.time.Duration

abstract class RpJob(
    val name: String,
    val delay: Duration
) {
    private val supervisor: CompletableJob
    private val scope: CoroutineScope

    init {
        val (supervisor, scope) = buildCoroutineScope(name)

        this.supervisor = supervisor
        this.scope = scope
    }

    fun start() = scope.launch {
        while (isActive) {
            tick()
            delay(delay)
        }
    }

    suspend fun stop() {
        supervisor.cancelAndJoin()
    }

    abstract suspend fun tick()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RpJob) return false

        if (name != other.name) return false
        if (delay != other.delay) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + delay.hashCode()
        return result
    }
}

private val log = logger()

private fun buildCoroutineScope(name: String): Pair<CompletableJob, CoroutineScope> {
    val supervisor = SupervisorJob()
    val scope =
        CoroutineScope(supervisor + CoroutineName(name) + CoroutineExceptionHandler { _, throwable ->
            if (throwable is CancellationException) return@CoroutineExceptionHandler

            log.atSevere().withCause(throwable).log("$name failed")
        })

    return supervisor to scope
}