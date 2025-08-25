package dev.slne.surf.roleplay.core.common.transaction

import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.core.common.transaction.utils.BalanceType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import java.util.*

@Serializable
data class RpTransaction(
    val uniqueId: @Contextual UUID,
    val sender: RpPlayer?,
    val receiver: RpPlayer?,
    val amount: Int,
    val balanceType: BalanceType,
    val description: String? = null,
    val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now()
)