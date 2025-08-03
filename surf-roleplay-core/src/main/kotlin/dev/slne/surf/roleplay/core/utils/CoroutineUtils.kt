package dev.slne.surf.roleplay.core.utils

import dev.slne.surf.surfapi.core.api.util.logger
import kotlinx.coroutines.*

private val log = logger()

fun buildCoroutineScope(name: String): Pair<CompletableJob, CoroutineScope> {
    val supervisor = SupervisorJob()
    val scope =
        CoroutineScope(supervisor + CoroutineName(name) + CoroutineExceptionHandler { _, throwable ->
            if (throwable is CancellationException) return@CoroutineExceptionHandler

            log.atSevere().withCause(throwable).log("$name failed")
        })

    return supervisor to scope
}