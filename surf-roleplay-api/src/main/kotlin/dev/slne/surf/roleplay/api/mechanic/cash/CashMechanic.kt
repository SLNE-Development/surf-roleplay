package dev.slne.surf.roleplay.api.mechanic.cash

import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.roleplay.api.mechanic.cash.utils.AddCashToInventoryResult
import dev.slne.surf.roleplay.api.mechanic.cash.utils.CashInInventoryResult
import dev.slne.surf.roleplay.api.mechanic.cash.utils.RemoveCashFromInventoryResult
import dev.slne.surf.roleplay.api.player.RpPlayer

interface CashMechanic : Mechanic {
    /**
     * Adds the specified amount of cash to the player's balance.
     *
     * @param player The player to whom the cash balance will be added.
     * @param amount The amount of cash to add. This should be a positive integer.
     * @throws IllegalArgumentException if the amount is negative or zero.
     * @return [AddCashToInventoryResult] indicating the result of the operation.
     */
    fun addCashBalance(player: RpPlayer, amount: Int): AddCashToInventoryResult

    /**
     * Removes the specified amount of cash from the player's balance.
     *
     * @param player The player from whose cash balance the amount will be removed.
     * @param amount The amount of cash to remove. This should be a positive integer.
     * @return [RemoveCashFromInventoryResult] indicating the result of the operation.
     * @throws IllegalArgumentException if the amount is negative or zero.
     */
    fun removeCashBalance(player: RpPlayer, amount: Int): RemoveCashFromInventoryResult

    /**
     * Retrieves the current cash balance of the specified player.
     *
     * @param player The player whose cash balance is to be retrieved.
     * @return [CashInInventoryResult] containing the player's cash balance and the items representing that balance.
     */
    fun getCashBalance(player: RpPlayer): CashInInventoryResult
}