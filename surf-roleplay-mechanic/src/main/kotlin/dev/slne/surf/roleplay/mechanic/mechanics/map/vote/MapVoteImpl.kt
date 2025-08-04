package dev.slne.surf.roleplay.mechanic.mechanics.map.vote

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import dev.slne.surf.roleplay.api.coroutine.RpJob
import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.roleplay.api.mechanic.map.Map
import dev.slne.surf.roleplay.api.mechanic.map.MapMechanic
import dev.slne.surf.roleplay.api.mechanic.map.event.MapVoteCastEvent
import dev.slne.surf.roleplay.api.mechanic.map.event.MapVoteEndedEvent
import dev.slne.surf.roleplay.api.mechanic.map.event.MapVoteStartedEvent
import dev.slne.surf.roleplay.api.mechanic.map.vote.MapVote
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.RpPlayerManager
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.messages.adventure.*
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.util.*
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.bukkit.Sound
import org.checkerframework.common.value.qual.IntRange
import org.jetbrains.annotations.UnmodifiableView
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.asKotlinRandom
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toKotlinDuration

class MapVoteImpl(
    override val votingDuration: Duration
) : RpJob(
    name = "MapVote",
    delay = 1.seconds
), MapVote {

    lateinit var startedAt: ZonedDateTime

    private val _votes = ConcurrentHashMap<Map, ObjectList<UUID>>()
    override val votes get() = ConcurrentHashMap(_votes)

    override suspend fun tick() {
        if (checkIfVoteEnded()) {
            endTimeout()
            return
        }

        val onlinePlayers = RpPlayerManager.players
        val alreadyVotesPlayers = _votes.values.flatten().toSet()

        val playersToVote = onlinePlayers
            .filterNot { alreadyVotesPlayers.contains(it.uuid) }.toObjectSet()

        if (playersToVote.isEmpty() && _votes.isEmpty()) {
            endVote(MapVote.MapVoteEndedReason.NoVotes)
            return
        }

        if (playersToVote.isEmpty()) {
            endTimeout()
            return
        }

        showVoteScreen(playersToVote)
    }

    private fun showVoteScreen(playersToVote: ObjectSet<RpPlayer>) {
        playersToVote.forEach { player ->
            player.bukkitPlayer?.sendText {
                appendPrefix()

                info("Jetzt vote endlich mal für eine Map!")
            }
        }
    }

    private suspend fun endTimeout() {
        val maps = selectMapsByHighestVote()

        val (map, votes) = if (maps.size > 1) {
            endTieBreak()
        } else {
            maps.keys.first() to maps.values.first()
        }

        endVote(MapVote.MapVoteEndedReason.MapSelected(map, votes))
    }

    private suspend fun endTieBreak(): Pair<Map, Int> {
        val mapsByHighestVotes = selectMapsByHighestVote()
        val (map, votes) = mapsByHighestVotes.entries.random(random.asKotlinRandom())

        showVoteTieBreakScreen(mapsByHighestVotes, map)

        return map to votes
    }

    private fun checkIfVoteEnded() = ::startedAt.isInitialized &&
            ZonedDateTime.now().isAfter(startedAt.plusSeconds(votingDuration.inWholeSeconds))

    private fun selectMapsByHighestVote(): @UnmodifiableView Object2ObjectMap<Map, Int> {
        var maxVotes = 0
        val topMaps = mutableObject2ObjectMapOf<Map, Int>()

        for ((map, voters) in _votes) {
            val voteCount = voters.size

            when {
                voteCount > maxVotes -> {
                    topMaps.clear()
                    topMaps.put(map, voteCount)
                    maxVotes = voteCount
                }

                voteCount == maxVotes -> {
                    topMaps.put(map, voteCount)
                }
            }
        }

        return topMaps.freeze()
    }

    override suspend fun startVote() {
        startedAt = ZonedDateTime.now()

        start()

        withContext(plugin.globalRegionDispatcher) {
            MapVoteStartedEvent().callEvent()
        }
    }

    private suspend fun showVoteTieBreakScreen(
        possibleMaps: Object2ObjectMap<Map, Int>,
        resultingMap: Map
    ) {
        delay(1.seconds)
    }

    private suspend fun showVoteEndedScreen(resultingMap: Map, votes: Int) {
        showTitle({
            primary("Map Vote")
        }, {
            secondary("Die Abstimmung ist beendet!")
        }, {
            fadeIn(250.milliseconds)
            stay(3.seconds)
            fadeOut(250.milliseconds)
        })

        delay(1.seconds)

        server.onlinePlayers.forEach { player ->
            withContext(plugin.entityDispatcher(player)) {
                player.sendText {
                    appendPrefix()

                    info("Gewonnen hat die Map: ")
                    append(resultingMap)
                    info(" mit ")
                    variableValue(votes)
                    info(" Stimmen. Der Server wird in Kürze neu gestartet.")
                }

                player.playSound(true) {
                    type(Sound.ENTITY_PLAYER_LEVELUP)
                    volume(.5f)
                    source(net.kyori.adventure.sound.Sound.Source.PLAYER)
                }
            }
        }
    }

    private suspend fun showTitle(
        title: SurfComponentBuilder.() -> Unit,
        subtitle: SurfComponentBuilder.() -> Unit,
        times: @TitleTimeDsl TitleTimesBuilder.() -> Unit
    ) {
        val buildTimes = titleTimes(times)

        server.onlinePlayers.forEach { player ->
            withContext(plugin.entityDispatcher(player)) {
                player.showTitle {
                    title { title() }
                    subtitle { subtitle() }
                    times(times)
                }
            }
        }

        val fadeIn = buildTimes.fadeIn().toKotlinDuration()
        val stay = buildTimes.stay().toKotlinDuration()
        val fadeOut = buildTimes.fadeOut().toKotlinDuration()

        delay(fadeIn + stay + fadeOut)
    }

    override suspend fun endVote(reason: MapVote.MapVoteEndedReason) {
        stop()

        val (map, votes) = reason.getMapFromReason()
        val (eventMap, eventVotes) = withContext(plugin.globalRegionDispatcher) {
            val event = MapVoteEndedEvent(map, votes, reason)
            event.callEvent()
            event.winningMap to event.totalVotes
        }

        val selectedMap = eventMap ?: Mechanic.getMechanic<MapMechanic>().maps
            .random(random.asKotlinRandom())

        showVoteEndedScreen(selectedMap, eventVotes)
    }

    override suspend fun castVote(
        player: RpPlayer,
        map: Map,
        modifier: (RpPlayer) -> @IntRange(from = 1, to = Int.MAX_VALUE.toLong()) Int
    ) {
        val voteCount = modifier(player)

        if (voteCount <= 0) {
            throw IllegalArgumentException("Vote count must be greater than 0")
        }

        val eventAmount = withContext(plugin.globalRegionDispatcher) {
            val event = MapVoteCastEvent(
                map,
                player,
                voteCount
            )

            if (!event.callEvent()) {
                event.amount
            } else {
                0
            }
        }

        _votes.compute(map) { _, existingVotes ->
            val updatedVotes = existingVotes?.toMutableObjectList() ?: mutableObjectListOf()

            repeat(eventAmount) {
                updatedVotes.add(player.uuid)
            }

            updatedVotes
        }
    }
}