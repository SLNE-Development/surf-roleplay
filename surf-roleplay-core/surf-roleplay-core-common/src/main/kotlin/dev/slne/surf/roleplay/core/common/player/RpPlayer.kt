package dev.slne.surf.roleplay.core.common.player

import dev.slne.surf.cloud.api.common.player.OfflineCloudPlayer
import dev.slne.surf.cloud.api.common.player.toOfflineCloudPlayer
import dev.slne.surf.roleplay.RoleplayApplication
import dev.slne.surf.roleplay.core.common.transaction.HasRpTransactions
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.ComponentLike
import org.springframework.beans.factory.getBean
import java.time.ZonedDateTime
import java.util.*

@Serializable(with = RpPlayerSerializer::class)
abstract class RpPlayer(
    val uuid: UUID,
    var createdAt: ZonedDateTime = ZonedDateTime.now(),
    var updatedAt: ZonedDateTime = ZonedDateTime.now()
) : HasRpTransactions, ComponentLike {
    val cloudPlayer: OfflineCloudPlayer
        get() = uuid.toOfflineCloudPlayer()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RpPlayer) return false

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }


    companion object {
        operator fun get(cloudPlayer: OfflineCloudPlayer) = get(cloudPlayer.uuid)
        operator fun get(uuid: UUID) =
            RoleplayApplication.context.getBean<RpPlayerManager>()
                .getPlayerByUuid(uuid)
    }

}

/**
 * Gets the [RpPlayer] associated with this [OfflineCloudPlayer].
 *
 * @return The [RpPlayer] instance for this player.
 */
fun OfflineCloudPlayer.rpPlayer() = RpPlayer[this]

object RpPlayerSerializer : KSerializer<RpPlayer> {
    override val descriptor = buildClassSerialDescriptor("RpPlayer") {
        element<Long>("mostSigBits")
        element<Long>("leastSigBits")
    }

    override fun serialize(
        encoder: Encoder,
        value: RpPlayer
    ) {
        val uuid = value.uuid
        encoder.encodeLong(uuid.mostSignificantBits)
        encoder.encodeLong(uuid.leastSignificantBits)
    }

    override fun deserialize(decoder: Decoder): RpPlayer {
        val mostSigBits = decoder.decodeLong()
        val leastSigBits = decoder.decodeLong()
        val uuid = UUID(mostSigBits, leastSigBits)
        return RpPlayer[uuid]
    }
}