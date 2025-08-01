package dev.slne.surf.roleplay.api.transaction

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import java.time.ZonedDateTime
import java.util.*

interface RpTransaction {

    /**
     * Unique identifier for the transaction.
     */
    val uniqueId: UUID

    /**
     * The sender of the transaction.
     */
    val sender: RpPlayer?

    /**
     * The receiver of the transaction.
     */
    val receiver: RpPlayer?

    /**
     * The amount of money involved in the transaction.
     */
    val amount: Double

    /**
     * The type of balance this transaction affects.
     */
    val balanceType: BalanceType

    /**
     * A description of the transaction, providing additional context or details.
     */
    val description: String?

    /**
     * The timestamp when the transaction was created.
     */
    val createdAt: ZonedDateTime

}